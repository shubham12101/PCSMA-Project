import datetime
import urllib
import pycurl
import StringIO

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

def get_location():
	api_data = curl_request_addr("https://192.168.1.40:9119","/count?at=" + get_current_time()+ "&format=yyyy-mm-dd-hh24:mi:ss&type=bfwr")
	return api_data

def get_current_time():
        print "newTime"
        nowTime = datetime.datetime.now()
        url_time=nowTime.strftime("%Y-%m-%d-%H:%M:%S")
        return url_time

def curl_request_addr(address,url):
        auth_token = "7843766bd497fa781e2cbf88bb84c3a796b7825e00307f1dcaa5a511e22a0520"
        url = urllib.quote(url,safe="%/:=&?~#+!$,;'@()*[]")
        api_data_url = address+url+"&token="+auth_token
        api_data_url = str(api_data_url.encode('utf-8')) # done to fix on server
        print api_data_url
        c = pycurl.Curl()
        c.setopt(pycurl.URL, api_data_url)
        c.setopt(pycurl.SSL_VERIFYPEER, 0)
        c.setopt(pycurl.SSL_VERIFYHOST, 0)
        b = StringIO.StringIO()
        c.setopt(pycurl.WRITEFUNCTION, b.write)
        c.setopt(pycurl.FOLLOWLOCATION, 1)
        c.setopt(pycurl.MAXREDIRS, 5)
        c.perform()
        api_data = b.getvalue()
        return api_data

def get_max_ppl(locations):
	max_count=0
	max_location={}
	for location in locations:
		loc_count=int(location['count'])
		if(loc_count>max_count):
			max_count=loc_count
			max_location=location
	return max_location

def get_min_ppl(locations):
	min_count=locations[0]['count']
	min_location={}
	for location in locations:
		loc_count=int(location['count'])
		if(loc_count<min_count):
			min_count=loc_count
			min_location=location
	return min_location