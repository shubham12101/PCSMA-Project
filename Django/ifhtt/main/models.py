from django.db import models
from django.conf import settings
from django.contrib.auth.models import AbstractBaseUser,BaseUserManager, User
from django.db.models.signals import post_save
from django.dispatch import receiver
from gcm.models import AbstractDevice

class UserDevice(AbstractDevice):
    pass

class Menu(models.Model):
	day = models.CharField(max_length=200)
	items = models.CharField(max_length=200)
	time_slot = models.CharField(max_length=200)

	class Meta:
		db_table = u'menu'

class Course(models.Model):
	course_id = models.CharField(max_length=200, unique=True)
	name = models.CharField(max_length=200)
	website = models.CharField(max_length=200)

	class Meta:
		db_table = u'course'