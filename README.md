[![Build status](https://ci.appveyor.com/api/projects/status/t7y6wnmbgj987n7s?svg=true)](https://ci.appveyor.com/project/yoursalex/homeworkaqapatterns)

## *Домашнее задание №5.*

### В ходе выполнения домашнего задания были выполнены следующие задачи: 

* Реализована возможность выбрать город из массива валидных городов. Данный список может содержать и большее кол-во.
* Реализована возможность выбрать не валидный город из дополнительного массива
* Использован faker для имени и номера телефона. В случае с невалидным именем использована английская локаль. В случае, когда должен быть невалидный телефонный номер, к сожалению, ничего лучше, чем захардкодить тоже не придумала. Наверное, можно было тоже написать какой-то массив? 
* В Issue занесены баги, встретившиеся в процессе подготовки тестов
* Тест с телефонным номером НАМЕРЕННО не проходит, чтобы наглядно продемонстрировать баг. 


### Вопросы: 

* Правильно ли я понимаю, что когда мы реализуем возможность генерации данных, речи о параметризированных тестах уже не идет? Потому что у меня получилось много тестов, не уверена, что это правильно