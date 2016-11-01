#!/usr/bin/env python
import rospy
import sys
from tablet_socket.msg import route_cmd, mode_cmd, gear_cmd
from car_functions.msg import power,stop, park
from car_functions.msg import lock_doors, windows, pump_door
from car_functions.msg import record,safe_energy, sos
from car_functions.msg import air_conditioner,inside_light
from car_functions.msg import seats,alert,weather
from car_functions.msg import angle
from car_functions.msg import energy_remaining
from nmea_msgs.msg import Sentence
from std_msgs.msg import Float32
from geometry_msgs.msg import TwistStamped

import time

def intTryParse(value):
    try:
        return int(value)
    except ValueError:
        return 0	
def floatTryParse(value):
    try:
        return float(value)
    except ValueError:
        return value
def printMessage(msg):
	print('*****************************************')
	print(msg)
	print('*****************************************')


def main():
	not_exit_l1 = True
	rospy.init_node('Example_Data', anonymous=True)
	while (not_exit_l1):
		time.sleep(0.5)
		print("--------------------------------------------------------------------------------------------------------------")
		print("Welcom here you can read and send data of the node ros and the android app for the challenge 4 of Udacity!")
		print("1) route Topic: /route (Only read)") 
		print("2) GPS Topic: /nmea_sentence (Read from autoware demo)")
		print("3) Velocity Topic: /estimated_vel_kmph (Read from autoware demo)")
		print("4) Angle Topic / (Read and Write)")
		print("5) Direction Topic /estimate_twist (Read from autoware demo)")
		print("6) Energy Remaining Topic /basic_function/energy_remaining (Read and Write)")
		print("7) Weather Topic /basic_function/weather (Read and Write)")
		print("8) Alert Topic /basic_function/lock_doors (Read and Write)")
		print("9) Gear Topic: /gear_cmd (Read and Write)")
		print("10) Lock Doors Topic: /basic_function/lock_doors (Read and Write)")
		print("11) Pump Door Topic: /basic_function/pump_door (Read and Write)")
		print("12) Windows Topic: /basic_function/windows (Read and Write)")
		print("13) Record Topic: /basic_function/rec (Read and Write)")
		print("14) Safe Energy or Velocity Topic: /basic_function/safe_energy (Read and Write)")
		print("15) S.O.S Topic: /basic_function/sos (Read and Write)")
		print("16) Air Conditioner Topic /basic_function/air_conditioner (Read and Write)")
		print("17) Inside Lights Topic /basic_function/inside_lights (Read and Write)")
		print("18) Seats Topic /basic_function/seats (Read and Write)")
		print("To get out type exit")
		selected = raw_input("Please select one: ")
		if(selected.upper() == 'EXIT'):
			sys.exit()

		#1) Route
		# tablet_socket::route_cmd msg;
		# tablet_socket::Waypoint point;
		if(intTryParse(selected) == 1):
			print("Press Ctrl + C to go back")
			rospy.Subscriber("/route",route_cmd, printMessage)
			rospy.spin()
			time.sleep(20)


		#2) GPS
		#if you print only sentnce give the information
		if(intTryParse(selected) == 2):
			print("Press Ctrl + C to go back")
			rospy.Subscriber("nmea_sentence",Sentence, printMessage)
			rospy.spin()
			time.sleep(20)
		#3) Velocity
		if(intTryParse(selected) == 3):
			print("Press Ctrl + C to go back")
			rospy.Subscriber("/estimated_vel_kmph",Float32, printMessage)
			rospy.spin()
			time.sleep(20)

		#4) Angle
		if(intTryParse(selected) == 4):
			print("1) Read")
			print("2) Write")
			print("3) Exit")
			selected_l2 = raw_input("Select: ")
			if ( intTryParse(selected_l2) == 1 ):
				print("Press Ctrl + C to go back")
				rospy.Subscriber("/basic_function/angle",angle, printMessage)
				rospy.spin()
				time.sleep(20)
			elif ( intTryParse(selected_l2) == 2 ):
				publication = rospy.Publisher('/basic_function/angle',angle, queue_size=10)
				rate = rospy.Rate(10) # 10hz
				while(publication.get_num_connections()==0):
						print("Waiting a connection...")
						rate.sleep()
				msg = angle()
				selected_l3 = raw_input("Send the angle of the car (from 0 to 360): ")
				if( intTryParse(selected_l3) >= 0 and intTryParse(selected_l3) <= 360 ):
					msg.angle = intTryParse(selected_l3)
					publication.publish(msg)
					rate.sleep()
					print('****************** Send! ****************** ')
				else:
					print ("Error! You go back")
			else:
				print ("Error! You go back")
		#5) estimate_twist
		if(intTryParse(selected) == 5):
			print("Press Ctrl + C to go back")
			rospy.Subscriber("/estimate_twist",TwistStamped, printMessage)
			rospy.spin()
			time.sleep(20)
		#6) Energy Remaining
		if(intTryParse(selected) == 6):
			print("1) Read")
			print("2) Write")
			print("3) Exit")
			selected_l2 = raw_input("Select: ")
			if ( intTryParse(selected_l2) == 1 ):
				print("Press Ctrl + C to go back")
				rospy.Subscriber("/basic_function/energy_remaining",energy_remaining, printMessage)
				rospy.spin()
				time.sleep(20)
			elif ( intTryParse(selected_l2) == 2 ):
				publication = rospy.Publisher('/basic_function/energy_remaining',energy_remaining, queue_size=10)
				rate = rospy.Rate(10) # 10hz
				while(publication.get_num_connections()==0):
						print("Waiting a connection...")
						rate.sleep()
				msg = energy_remaining()
				selected_l3 = raw_input("Send the energy remaining in Km: ")
				if( intTryParse(selected_l3) >= 0 ):
					msg.energy = intTryParse(selected_l3)
					publication.publish(msg)
					rate.sleep()
					print('****************** Send! ****************** ')
				else:
					print ("Error! You go back")
			else:
				print ("Error! You go back")
		

		#7) Weather Topic
		if(intTryParse(selected) == 7):
			print("1) Read")
			print("2) Write")
			print("3) Exit")
			selected_l2 = raw_input("Select: ")
			if ( intTryParse(selected_l2) == 1 ):
				print("Press Ctrl + C to go back")
				rospy.Subscriber("/basic_function/weather",weather, printMessage)
				rospy.spin()
				time.sleep(20)
			elif ( intTryParse(selected_l2) == 2 ):
				publication = rospy.Publisher('/basic_function/weather',weather, queue_size=10)
				rate = rospy.Rate(10) # 10hz
				while(publication.get_num_connections()==0):
						print("Waiting a connection...")
						rate.sleep()
				msg = weather()
				selected_l3 = raw_input("Send the weather outside of the car in degree: ")
				if( intTryParse(selected_l3) <>0 ):
					msg.weather = intTryParse(selected_l3)
					publication.publish(msg)
					rate.sleep()
					print('****************** Send! ****************** ')
				else:
					print ("Error! You go back")
			else:
				print ("Error! You go back")
		

		#8) Alert
		if(intTryParse(selected) == 8):
			print("1) Read")
			print("2) Write")
			print("3) Exit")
			selected_l2 = raw_input("Select: ")
			if ( intTryParse(selected_l2) == 1 ):
				print("Press Ctrl + C to go back")
				rospy.Subscriber("/basic_function/alert",sos, printMessage)
				rospy.spin()
				time.sleep(20)
			elif ( intTryParse(selected_l2) == 2 ):
				publication = rospy.Publisher('/basic_function/alert',sos, queue_size=10)
				rate = rospy.Rate(10) # 10hz
				while(publication.get_num_connections()==0):
						print("Waiting a connection...")
						rate.sleep()
				msg = alert()
				selected_l3 = raw_input("Select 0 from no alert, 1 to 5 from alerts: ")
				if( intTryParse(selected_l3) >= 0 and intTryParse(selected_l3) <= 5 ):
					msg.sos = intTryParse(selected_l3)
					publication.publish(msg)
					rate.sleep()
					print('****************** Send! ****************** ')
				else:
					print ("Error! You go back")
			else:
				print ("Error! You go back")
		
		#9) GEAR
		if(intTryParse(selected) == 9):
			print("1) Read")
			print("2) Write")
			print("3) Exit")
			selected_l2 = raw_input("Select: ")
			if ( intTryParse(selected_l2) == 1 ):
				print("Press Ctrl + C to go back")
				rospy.Subscriber("/gear_cmd",gear_cmd, printMessage)
				rospy.spin()
				rospy.Subscriber("/mode_cmd",mode_cmd, None)
				rospy.spin()

				time.sleep(50)
			elif ( intTryParse(selected_l2) == 2 ):
				publication_mode = rospy.Publisher('/mode_cmd',mode_cmd, queue_size=10)
				rate = rospy.Rate(10) # 10hz
				while(publication_mode.get_num_connections()==0):
						print("Waiting a connection...")
						rate.sleep()
				msg = mode_cmd()
				msg.mode = 1
				publication_mode.publish(msg)

				publication = rospy.Publisher('/gear_cmd',gear_cmd, queue_size=10)
				while(publication.get_num_connections()==0):
						print("Waiting a connection...")
						rate.sleep()
				msg = gear_cmd()
				selected_l3 = raw_input("Select 1 Drive, 2 retro, 3 break, 4 neutro from no alert: ")
				if( intTryParse(selected_l3) >= 1 and intTryParse(selected_l3) <= 4 ):
					msg.gear = intTryParse(selected_l3)
					publication.publish(msg)
					rate.sleep()
					print('****************** Send! ****************** ')
				else:
					print ("Error! You go back")
			else:
				print ("Error! You go back")

		#10) Lock Door
		if(intTryParse(selected) == 10):
			print("1) Read")
			print("2) Write")
			print("3) Exit")
			selected_l2 = raw_input("Select: ")
			if ( intTryParse(selected_l2) == 1 ):
				print("Press Ctrl + C to go back")
				rospy.Subscriber("/basic_function/lock_doors",lock_doors, printMessage)
				rospy.spin()
				time.sleep(20)
			elif ( intTryParse(selected_l2) == 2 ):
				publication = rospy.Publisher('/basic_function/lock_doors',lock_doors, queue_size=10)
				rate = rospy.Rate(10) # 10hz
				while(publication.get_num_connections()==0):
						print("Waiting a connection...")
						rate.sleep()
				msg = lock_doors()
				selected_l3 = raw_input("Select TRUE to lock the doors and False to unlock: ")
				if( selected_l3.upper() == "TRUE"):
					msg.lock = True
					publication.publish(msg)
					rate.sleep()
					print('****************** Send! ****************** ')
				elif( selected_l3.upper() == "FALSE"):
					msg.lock = False
					publication.publish(msg)
					rate.sleep()
					print('****************** Send! ****************** ')
				else:
					print ("Error! You go back")
			else:
				print ("Error! You go back")
		
		#11) Pump Door
		if(intTryParse(selected) == 11):
			print("1) Read")
			print("2) Write")
			print("3) Exit")
			selected_l2 = raw_input("Select: ")
			if ( intTryParse(selected_l2) == 1 ):
				print("Press Ctrl + C to go back")
				rospy.Subscriber("/basic_function/pump_door",pump_door, printMessage)
				rospy.spin()
				time.sleep(20)
			elif ( intTryParse(selected_l2) == 2 ):
				publication = rospy.Publisher('/basic_function/pump_door',pump_door, queue_size=10)
				rate = rospy.Rate(10) # 10hz
				while(publication.get_num_connections()==0):
						print("Waiting a connection...")
						rate.sleep()
				msg = pump_door()
				selected_l3 = raw_input("Select False to unlock: ")
				if( selected_l3.upper() == "FALSE"):
					msg.lock = False
					publication.publish(msg)
					rate.sleep()
					print('****************** Send! ****************** ')
				else:
					print ("Error! You go back")
			else:
				print ("Error! You go back")

		#12) Windows Down and up
		if(intTryParse(selected) == 12):
			print("1) Read")
			print("2) Write")
			print("3) Exit")
			selected_l2 = raw_input("Select: ")
			if ( intTryParse(selected_l2) == 1 ):
				print("Press Ctrl + C to go back")
				rospy.Subscriber("/basic_function/windows",windows, printMessage)
				rospy.spin()
				time.sleep(20)
			elif ( intTryParse(selected_l2) == 2 ):
				publication = rospy.Publisher('/basic_function/windows',windows, queue_size=10)
				rate = rospy.Rate(10) # 10hz
				while(publication.get_num_connections()==0):
						print("Waiting a connection...")
						rate.sleep()
				msg = windows()
				print("Insert value in a range 0 to 10 (10 is the windows close)")
				selected_l3 = raw_input("Ahead left Window: ")
				if( floatTryParse(selected_l3)>= 0 and floatTryParse(selected_l3)<=10 ):
					msg.ahead_left = floatTryParse(selected_l3)
				else:
						msg.ahead_left = 10
				selected_l3 = raw_input("Ahead right Window: ")
				if( floatTryParse(selected_l3)>= 0 and floatTryParse(selected_l3)<=10 ):
					msg.ahead_right = floatTryParse(selected_l3)
				else:
					msg.ahead_right = 10
				selected_l3 = raw_input("Back left Window: ")
				if( floatTryParse(selected_l3)>= 0 and floatTryParse(selected_l3)<=10 ):
					msg.back_left = floatTryParse(selected_l3)
				else:
					msg.back_left = 10
				selected_l3 = raw_input("Back right Window: ")
				if( floatTryParse(selected_l3)>= 0 and floatTryParse(selected_l3)<=10 ):
					msg.back_right = floatTryParse(selected_l3)
				else:
					msg.back_right = 10
				publication.publish(msg)
				rate.sleep()
				print('****************** Send! ****************** ')
			else:
				print ("Error! You go back")
		#13) record and Stop recording
		if(intTryParse(selected) == 13):
			print("1) Read")
			print("2) Write")
			print("3) Exit")
			selected_l2 = raw_input("Select: ")
			if ( intTryParse(selected_l2) == 1 ):
				print("Press Ctrl + C to go back")
				rospy.Subscriber("/basic_function/rec",record, printMessage)
				rospy.spin()
				time.sleep(20)
			elif ( intTryParse(selected_l2) == 2 ):
				publication = rospy.Publisher('/basic_function/rec',record, queue_size=10)
				rate = rospy.Rate(10) # 10hz
				while(publication.get_num_connections()==0):
						print("Waiting a connection...")
						rate.sleep()
				msg = record()
				selected_l3 = raw_input("Select TRUE to star recording and FALSE to pause: ")
				if( selected_l3.upper() == "TRUE"):
					msg.rec = True
					publication.publish(msg)
					rate.sleep()
					print('****************** Send! ****************** ')
				elif( selected_l3.upper() == "FALSE"):
					msg.rec = False
					publication.publish(msg)
					rate.sleep()
					print('****************** Send! ****************** ')
				else:
					print ("Error! You go back")
			else:
				print ("Error! You go back")

		#14) Safe Energy or Velocity 
		if(intTryParse(selected) == 14):
			print("1) Read")
			print("2) Write")
			print("3) Exit")
			selected_l2 = raw_input("Select: ")
			if ( intTryParse(selected_l2) == 1 ):
				print("Press Ctrl + C to go back")
				rospy.Subscriber("/basic_function/safe_energy",safe_energy, printMessage)
				rospy.spin()
				time.sleep(20)
			elif ( intTryParse(selected_l2) == 2 ):
				publication = rospy.Publisher('/basic_function/safe_energy',safe_energy, queue_size=10)
				rate = rospy.Rate(10) # 10hz
				while(publication.get_num_connections()==0):
						print("Waiting a connection...")
						rate.sleep()
				msg = safe_energy()
				selected_l3 = raw_input("Select TRUE to safe energy or FALSE to velocity: ")
				if( selected_l3.upper() == "TRUE"):
					msg.safe = True
					publication.publish(msg)
					rate.sleep()
					print('****************** Send! ****************** ')
				elif( selected_l3.upper() == "FALSE"):
					msg.safe = False
					publication.publish(msg)
					rate.sleep()
					print('****************** Send! ****************** ')
				else:
					print ("Error! You go back")
			else:
				print ("Error! You go back")
		
		#15) S.O.S
		if(intTryParse(selected) == 15):
			print("1) Read")
			print("2) Write")
			print("3) Exit")
			selected_l2 = raw_input("Select: ")
			if ( intTryParse(selected_l2) == 1 ):
				print("Press Ctrl + C to go back")
				rospy.Subscriber("/basic_function/sos",sos, printMessage)
				rospy.spin()
				time.sleep(20)
			elif ( intTryParse(selected_l2) == 2 ):
				publication = rospy.Publisher('/basic_function/sos',sos, queue_size=10)
				rate = rospy.Rate(10) # 10hz
				while(publication.get_num_connections()==0):
						print("Waiting a connection...")
						rate.sleep()
				msg = sos()
				selected_l3 = raw_input("Select TRUE to send sos signal. FALSE normaly: ")
				if( selected_l3.upper() == "TRUE"):
					msg.sos = True
					publication.publish(msg)
					rate.sleep()
					print('****************** Send! ****************** ')
				elif( selected_l3.upper() == "FALSE"):
					msg.sos = False
					publication.publish(msg)
					rate.sleep()
					print('****************** Send! ****************** ')
				else:
					print ("Error! You go back")
			else:
				print ("Error! You go back")

		#16) Air conditioner 
		if(intTryParse(selected) == 16):
			print("1) Read")
			print("2) Write")
			print("3) Exit")
			selected_l2 = raw_input("Select: ")
			if ( intTryParse(selected_l2) == 1 ):
				print("Press Ctrl + C to go back")
				rospy.Subscriber("/basic_function/air_conditioner",air_conditioner, printMessage)
				rospy.spin()
				time.sleep(20)
			elif ( intTryParse(selected_l2) == 2 ):
				publication = rospy.Publisher('/basic_function/air_conditioner',air_conditioner, queue_size=10)
				rate = rospy.Rate(10) # 10hz
				while(publication.get_num_connections()==0):
						print("Waiting a connection...")
						rate.sleep()
				msg = air_conditioner()
				selected_l3 = raw_input("Select a range from 16 to 25: ")
				if(  intTryParse(selected_l3) >= 16 and intTryParse(selected_l3) <= 25 ):
					msg.temp = intTryParse(selected_l3)
					publication.publish(msg)
					rate.sleep()
					print('****************** Send! ****************** ')
				else:
					print ("Error! You go back")
			else:
				print ("Error! You go back")
		#17) Inside Lights  /basic_function/inside_lights
		if(intTryParse(selected) == 17):
			print("1) Read")
			print("2) Write")
			print("3) Exit")
			selected_l2 = raw_input("Select: ")
			if ( intTryParse(selected_l2) == 1 ):
				print("Press Ctrl + C to go back")
				rospy.Subscriber("/basic_function/inside_lights",inside_light, printMessage)
				rospy.spin()
				time.sleep(20)
			elif ( intTryParse(selected_l2) == 2 ):
				publication = rospy.Publisher('/basic_function/inside_lights',inside_light, queue_size=10)
				rate = rospy.Rate(10) # 10hz
				while(publication.get_num_connections()==0):
						print("Waiting a connection...")
						rate.sleep()
				msg = inside_light()
				selected_l3 = raw_input("Select TRUE to turn on or FALSE turn off the first light: ")
				if( selected_l3.upper() == "TRUE"):
					msg.frist_on = True
				elif( selected_l3.upper() == "FALSE"):
					msg.frist_on = False
				else:
					print ("Error! You go back")
				selected_l3 = raw_input("Select TRUE to turn on or FALSE turn off the second light: ")
				if( selected_l3.upper() == "TRUE"):
					msg.second_on = True
				elif( selected_l3.upper() == "FALSE"):
					msg.second_on = False
				else:
					print ("Error! You go back")
				selected_l3 = raw_input("Select TRUE to turn on or FALSE turn off the third light: ")
				if( selected_l3.upper() == "TRUE"):
					msg.third_on = True
				elif( selected_l3.upper() == "FALSE"):
					msg.third_on = False
				else:
					print ("Error! You go back")
				publication.publish(msg)
				rate.sleep()
				print('****************** Send! ****************** ')
			else:
				print ("Error! You go back")

		#18) Seats
		if(intTryParse(selected) == 18):
			print("1) Read")
			print("2) Write")
			print("3) Exit")
			selected_l2 = raw_input("Select: ")
			if ( intTryParse(selected_l2) == 1 ):
				print("Press Ctrl + C to go back")
				rospy.Subscriber("/basic_function/seats",seats, printMessage)
				rospy.spin()
				time.sleep(20)
			elif ( intTryParse(selected_l2) == 2 ):
				publication = rospy.Publisher('/basic_function/seats',seats, queue_size=10)
				rate = rospy.Rate(10) # 10hz
				while(publication.get_num_connections()==0):
						print("Waiting a connection...")
						rate.sleep()
				msg = seats()
				selected_l3 = raw_input("Select from a range 0 to 10 for the inclination of the ahead left: ")
				if( intTryParse(selected_l3) >=0 and intTryParse(selected_l3) <=10 ):
					msg.ahead_left = intTryParse(selected_l3)
				else:
					print ("Error! You go back")
				selected_l3 = raw_input("Select from a range 0 to 10 for the inclination of the ahead right: ")
				if( intTryParse(selected_l3) >=0 and intTryParse(selected_l3) <=10 ):
					msg.ahead_right = intTryParse(selected_l3)
				else:
					print ("Error! You go back")
				publication.publish(msg)
				rate.sleep()
				print('****************** Send! ****************** ')
			else:
				print ("Error! You go back")
if __name__ == '__main__':
	try:
		main()
	except rospy.ROSInterruptException:
		pass
