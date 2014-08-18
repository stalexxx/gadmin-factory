/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.ui.constants;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * BaseMessages - gwt messages' class
 *
 * @author Alexander Ostrovskiy (a.ostrovskiy)
 * @since 25.03.13
 */
public interface BaseMessages extends Messages {

    /*===========================================[ CLASS METHODS ]================*/

    //    buttons
    @DefaultMessage("Назад")
    String back();

    @DefaultMessage("Сохранить")
    String save();

    @DefaultMessage("Оценить")
    String evaluate();

    @DefaultMessage("Добавить")
    String add();

    @DefaultMessage("Редактировать")
    String edit();

    @DefaultMessage("Просмотреть")
    String view();

    @DefaultMessage("Отменить")
    String cancel();


    @DefaultMessage("Название")
    String name();

    @DefaultMessage("{0} / {1}")
    SafeHtml bySlash(String name, String fullName);

    //@DefaultMessage("<div>{0}<div style=\"color:#000000;font-style: italic;\">{1}</div></div>")
    @DefaultMessage("{0}</br><i>{1}</i>")
    SafeHtml twoLines(String name, String fullName);

    @DefaultMessage("Комментарий")
    String comment();

    @DefaultMessage("Email")
    String email();

    @DefaultMessage("Телефон")
    String phoneNumber();

    @DefaultMessage("Контактное лицо")
    String contactPerson();

    @DefaultMessage("Поиск...")
    String searchPlaceholder();



    @DefaultMessage("Дополнительно")
    String additional();



    //headings

    @DefaultMessage("Общие")
    String general();

    @DefaultMessage("Сбросить")
    String reset();




    @DefaultMessage("Создать")
    String create();

    @DefaultMessage("Удалить")
    String remove();

    @DefaultMessage("Справочники")
    String dictionaries();

    @DefaultMessage("{0}(Выйти)")
    String logout(String userName);

    @DefaultMessage("Логин")
    String login();

    @DefaultMessage("Да")
    String yes();

    @DefaultMessage("Нет")
    String no();

    @DefaultMessage("-- Не определено --")
    String getUndefined();



    @DefaultMessage("Закрыть")
    String close();

    @DefaultMessage("Применить")
    String apply();

    @DefaultMessage("C")
    String from();

    @DefaultMessage("По")
    String to();

    @DefaultMessage("Очистить")
    String clear();

    @DefaultMessage("Описание")
    String description();

    @DefaultMessage("Ошибка валидации: {0}")
    String validationFailed(String errorMessage);

    @DefaultMessage("Изображение")
    String image();

    @DefaultMessage("Объект не может быть загружен или найден")
    String cantFindObject();

    @DefaultMessage("Тип")
    String type();

    @DefaultMessage("Дата модификации")
    String modificationDate();

    @DefaultMessage("Кто обновил")
    String modificator();

    @DefaultMessage("Роли")
    String roles();

    @DefaultMessage("Пароль")
    String password();

    @DefaultMessage("Имя пользователя")
    String username();

    @DefaultMessage("Сохранен")
    String savedSuccessfull();

    @DefaultMessage("Количество элементов:")
    String totalElements();


}




