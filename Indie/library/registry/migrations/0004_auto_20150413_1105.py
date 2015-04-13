# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('registry', '0003_auto_20150413_1101'),
    ]

    operations = [
        migrations.AlterField(
            model_name='book',
            name='issue_date',
            field=models.DateField(null=True, verbose_name=b'date issued', blank=True),
            preserve_default=True,
        ),
    ]
