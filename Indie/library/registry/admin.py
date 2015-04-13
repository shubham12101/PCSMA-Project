from django.contrib import admin
from registry.models import Book

# Register your models here.
class BookAdmin(admin.ModelAdmin):
	list_display=('book_id','title','issue_date','issued_by')

admin.site.register(Book,BookAdmin)	