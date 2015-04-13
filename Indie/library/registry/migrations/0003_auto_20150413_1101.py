# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('registry', '0002_auto_20150413_1056'),
    ]

    operations = [
        migrations.AlterField(
            model_name='book',
            name='issue_date',
            field=models.DateTimeField(null=True, verbose_name=b'date issued', blank=True),
            preserve_default=True,
        ),
        migrations.AlterField(
            model_name='book',
            name='issued_by',
            field=models.EmailField(max_length=75, null=True, blank=True),
            preserve_default=True,
        ),
    ]
