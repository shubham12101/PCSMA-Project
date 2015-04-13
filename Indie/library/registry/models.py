from django.db import models

# Create your models here.
class Book(models.Model):
	book_id = models.CharField(max_length=200, unique=True)
	title = models.CharField(max_length=200)
	issue_date = models.DateField('date issued',null=True,blank=True)
	issued_by = models.EmailField(max_length=75,null=True,blank=True)

	class Meta:
		db_table = u'book'