"use strict";

var searchFormBtn = document.querySelector(".search-frm-btn");
var searchFormContainer = document.querySelector(".search-frm-container");
var searchBtn = document.querySelector(".search-send-frm-btn button[name='search']");

searchFormBtn.onclick = function () {
    showSearchElems();
};

function showSearchElems() {
    window.history.pushState(null, "search", "search");
    navBarBlock.style.display = "flex";
    updateFormContainer.style.display = "none";
    insideUpdateContactFormUpdCntctBtn.style.display = "none";
    imagePostBlock.style.display = "none";
    imageUpdBlock.style.dysplay = "none";
    createContactForm.style.display = "none";
    insideCreateContactFormCrtCntctBtn.style.display = "none";
    telephonesContainer.style.display = "none";
    displayAttachmentContainer.style.display = "none";
    pageBlock.style.display = "none";
    contactsTable.style.display = "block";
    searchFormContainer.style.display = "block";
}

function hideSearchElems() {
    searchFormContainer.style.display = "none";
}

//get data from input form
function getSearchFormElems() {
    return {
        firstname: document.querySelector(".search-frm-block input[name='firstname']"),
        lastname: document.querySelector(".search-frm-block input[name='lastname']"),
        patronymic: document.querySelector(".search-frm-block input[name='patronymic']"),
        birthdayCondition: document.querySelector(".search-frm-block select[name='date']"),
        birthdayDate: document.querySelector(".search-frm-block input[name='birthday']"),
        sex: document.querySelector(".search-frm-block select[name='sex']"),
        citizenship: document.querySelector(".search-frm-block input[name='citizenship']"),
        maritalStatus: document.querySelector(".search-frm-block input[name='maritalStatus']"),
        country: document.querySelector(".search-frm-block input[name='country']"),
        city: document.querySelector(".search-frm-block input[name='city']"),
        street: document.querySelector(".search-frm-block input[name='street']"),
        building: document.querySelector(".search-frm-block input[name='building']"),
        apartment: document.querySelector(".search-frm-block input[name='apartment']"),
        index: document.querySelector(".search-frm-block-last input[name='index']")
    }
}

function getURLBySearchFormElems(url, searchFormElems) {

    console.log(searchFormElems);

    url += '?';

    if (!(searchFormElems.firstname.value === "")) {
        url += '&firstname=' + searchFormElems.firstname.value;
    }
    if (!(searchFormElems.lastname.value === "")) {
        url += '&lastname=' + searchFormElems.lastname.value;
    }
    if (!(searchFormElems.patronymic.value === "")) {
        url += '&patronymic=' + searchFormElems.patronymic.value;
    }
    if (!(searchFormElems.birthdayCondition.value === "") && !(searchFormElems.birthdayDate.value === "")) {
        url += '&birthday=' + searchFormElems.birthdayCondition.value + '+' + searchFormElems.birthdayDate.value;
    }
    if (!(searchFormElems.sex.value === "")) {
        url += '&sex=' + searchFormElems.sex.value;
    }
    if (!(searchFormElems.citizenship.value === "")) {
        url += '&citizenship=' + searchFormElems.citizenship.value;
    }
    if (!(searchFormElems.maritalStatus.value === "")) {
        url += '&maritalStatus=' + searchFormElems.maritalStatus.value;
    }
    if (!(searchFormElems.country.value === "")) {
        url += '&country=' + searchFormElems.country.value;
    }
    if (!(searchFormElems.city.value === "")) {
        url += '&city=' + searchFormElems.city.value;
    }
    if (!(searchFormElems.street.value === "")) {
        url += '&street=' + searchFormElems.street.value;
    }
    if (!(searchFormElems.building.value === "")) {
        url += '&building=' + searchFormElems.building.value;
    }
    if (!(searchFormElems.apartment.value === "")) {
        url += '&apartment=' + searchFormElems.apartment.value;
    }
    if (!(searchFormElems.index.value === "")) {
        url += '&index=' + searchFormElems.index.value;
    }

    if (url.length > url.indexOf("?")) {
        url = url.replace('&', '');
    }

    console.log(url);
    return url;
}


searchBtn.onclick = function () {
    debugger;

    var searchFormElems = getSearchFormElems();

    var url = 'http://localhost:8080/directory/main/contacts';
    var paramentizedUrl = getURLBySearchFormElems(url, searchFormElems);

    getContactsAssynchRequest('GET', paramentizedUrl);

};

