from registry.models import Book
from rest_framework import serializers

class BookSerializer(serializers.HyperlinkedModelSerializer):
	class Meta:
		model = Book
		fields =('book_id','title','issue_date','issued_by')
