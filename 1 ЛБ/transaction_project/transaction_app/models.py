from django.db import models

class Category(models.Model):
    name        = models.CharField(max_length=100, verbose_name="Назва")
    description = models.CharField(max_length=200, blank=True,
                                   verbose_name="Опис")

    def __str__(self):
        return self.name

    class Meta:
        verbose_name_plural = "Категорії"

class Transaction(models.Model):
    TYPE_CHOICES = [
        ('debit',  'Дебет'),
        ('credit', 'Кредит'),
    ]
    description = models.CharField(max_length=200, verbose_name="Опис")
    amount      = models.DecimalField(max_digits=10, decimal_places=2,
                                      verbose_name="Сума")
    type        = models.CharField(max_length=10, choices=TYPE_CHOICES,
                                   verbose_name="Тип")
    category    = models.ForeignKey(Category, on_delete=models.SET_NULL,
                                    null=True, blank=True,
                                    verbose_name="Категорія")
    created_at  = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"{self.description} — {self.amount}"

    class Meta:
        ordering = ['-created_at']
