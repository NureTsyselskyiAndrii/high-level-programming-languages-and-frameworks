from django.test import TestCase
from django.urls import reverse
from .models import Transaction, Category

class TransactionViewsTestCase(TestCase):
    def setUp(self):
        self.cat1 = Category.objects.create(name='Продукти', description='Витрати на харчування')
        self.cat2 = Category.objects.create(name='Зарплата', description='Основне джерело доходу')

        self.t1 = Transaction.objects.create(
            description='Супермаркет',
            amount=350.50,
            type='debit',
            category=self.cat1
        )
        self.t2 = Transaction.objects.create(
            description='Аванс',
            amount=15000.00,
            type='credit',
            category=self.cat2
        )
        self.t_no_cat = Transaction.objects.create(
            description='Кишенькові витрати',
            amount=50.00,
            type='debit'
        )

    def test_home_view(self):
        response = self.client.get(reverse('home'))
        self.assertEqual(response.status_code, 200)
        self.assertTemplateUsed(response, 'transaction_app/home.html')
        self.assertContains(response, 'Супермаркет')
        self.assertContains(response, 'Аванс')
        self.assertContains(response, 'Кишенькові витрати')

    def test_by_category_view(self):
        response = self.client.get(reverse('by_category'))
        self.assertEqual(response.status_code, 200)
        self.assertTemplateUsed(response, 'transaction_app/by_category.html')
        self.assertContains(response, 'Продукти')
        self.assertContains(response, 'Зарплата')
        self.assertContains(response, 'Без категорії')
        self.assertContains(response, 'Кишенькові витрати')

    def test_add_transaction(self):
        response = self.client.post(reverse('home'), {
            'description': 'Кафе',
            'amount': '120.00',
            'type': 'debit',
            'category': self.cat1.id
        })
        self.assertEqual(response.status_code, 302) # Redirects on success
        self.assertEqual(Transaction.objects.filter(description='Кафе').count(), 1)
