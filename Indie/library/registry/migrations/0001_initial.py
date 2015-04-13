# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Book',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('book_id', models.CharField(unique=True, max_length=200)),
                ('title', models.CharField(max_length=200)),
                ('issue_date', models.DateTimeField(null=True, verbose_name=b'date issued')),
                ('issued_by', models.CharField(max_length=200, null=True)),
            ],
            options={
                'db_table': 'book',
            },
            bases=(models.Model,),
        ),
    ]
