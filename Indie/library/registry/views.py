from django.shortcuts import render
from rest_framework import status,viewsets
from rest_framework.views import APIView
from rest_framework.response import Response
from registry.serializers import BookSerializer
from registry.models import Book

# Create your views here.
class BookViewSet(APIView):
	queryset = Book.objects.all()
	serializer_class = BookSerializer

	def get(self, request, format=None):
		# Book_name=request.data.get('Book_name','')
		issued_by=request.GET.get('issued_by','')
		# print request.GET.get('sem')
		# print Book_name
		# print request.GET['Book_name']
		if(issued_by==''):
			serializer = BookSerializer(self.queryset, many=True)
		else:
			print issued_by
			serializer=BookSerializer(Book.objects.filter(issued_by=issued_by),many=True)
		return Response(serializer.data)

