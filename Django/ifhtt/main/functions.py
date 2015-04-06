import datetime

def get_time_slot(time):
	hour=datetime.datetime.fromtimestamp(time).hour
	print hour
	time_slot=""
	if(hour>=8 and hour<10):
		time_slot="Breakfast"
	elif(hour>=12 and hour<15):
		time_slot="Lunch"
	elif(hour>=16 and hour<18):
		time_slot="Snacks"
	elif(hour>=20 and hour<22):
		time_slot="Dinner"

	return time_slot

def get_day(time):
	week   = ['Monday', 
              'Tuesday', 
              'Wednesday', 
              'Thursday',  
              'Friday', 
              'Saturday',
              'Sunday']

  	day_no=datetime.datetime.fromtimestamp(time).weekday()

	return week[day_no]