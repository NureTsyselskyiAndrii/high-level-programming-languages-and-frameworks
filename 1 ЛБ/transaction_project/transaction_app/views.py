from django.shortcuts import render, redirect
from .models import Transaction, Category
from .forms import TransactionForm

def home(request):
    if request.method == 'POST':
        form = TransactionForm(request.POST)
        if form.is_valid():
            form.save()
            return redirect('home')
    else:
        form = TransactionForm()
    transactions = Transaction.objects.select_related('category').all()
    return render(request, 'transaction_app/home.html',
                  {'form': form, 'transactions': transactions})

def by_category(request):
    categories = Category.objects.prefetch_related('transaction_set').all()
    no_category = Transaction.objects.filter(category__isnull=True)
    return render(request, 'transaction_app/by_category.html',
                  {'categories': categories, 'no_category': no_category})
