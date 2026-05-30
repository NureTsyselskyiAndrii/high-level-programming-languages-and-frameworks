from django import forms
from .models import Transaction

class TransactionForm(forms.ModelForm):
    class Meta:
        model  = Transaction
        fields = ['description', 'amount', 'type', 'category']
        labels = {
            'description': 'Опис',
            'amount':      'Сума',
            'type':        'Тип',
            'category':    'Категорія',
        }
