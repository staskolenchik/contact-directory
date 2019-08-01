"use strict";

var sendEmailFormBtn = document.querySelector(".email-frm-btn");
var emailFormContainer = document.querySelector(".email-frm-container");
var sendEmailActionBtn = document.querySelector(".email-frm-btns-container button[name='email']");
var cancelEmailActionBtn = document.querySelectorAll(".email-frm-send-btn")[1];
var templateSelector = document.querySelector(".email-frm-template");

function showEmailForm() {
    navBarBlock.style.display = "none";
    updateFormContainer.style.display = "none";
    insideUpdateContactFormUpdCntctBtn.style.display = "none";
    imagePostBlock.style.display = "none";
    imageUpdBlock.style.dysplay = "none";
    createContactForm.style.display = "none";
    insideCreateContactFormCrtCntctBtn.style.display = "none";
    telephonesContainer.style.display = "none";
    displayAttachmentContainer.style.display = "none";
    pageBlock.style.display = "none";
    contactsTable.style.display = "none";
    searchFormContainer.style.display = "none";
    emailFormContainer.style.display = "block";
}

function hideEmailElements() {
    emailFormContainer.style.display = "none";
    pageBlock.style.display = "block";
    contactsTable.style.display = "block";
    navBarBlock.style.display = "flex";
}

cancelEmailActionBtn.onclick = function () {

    window.history.pushState(null, "home", "home");
    hideEmailElements();
};

sendEmailFormBtn.onclick = function () {
    stopPopupNotify();
    window.history.pushState(null, "email", "email");

    var selectedContactArray = getSelectedContactArray();

    var url = 'http://localhost:8080/directory/main/contacts';
    var queryString = getUrlWithIdParameters(selectedContactArray);
    if (queryString) {

        var method = 'GET';
        getEmailsAssynchRequest(method, url + queryString);
        showEmailForm();
    }
};

function getUrlWithIdParameters(idArray) {

    var queryString;

    if (idArray.length > 0) {
        queryString = '?';
        for (var i = 0; i < idArray.length; i++) {
            queryString += '&id' + '=' + idArray[i]
        }
        queryString = queryString.replace('&', '');
        return queryString;
    }

    queryString += "?id";

    return queryString;
}


function getEmailsAssynchRequest(method, url) {
    var XHR = new XMLHttpRequest();
    XHR.open(method, url);
    XHR.responseType = 'json';
    XHR.onreadystatechange = function () {
        if (XHR.readyState === 4 && XHR.status === 200) {
            console.log(XHR.response);
            var emailStr = getEmailString(XHR.response);
            fillEmailsTo(emailStr);
        } else if (XHR.readyState === 4 && XHR.status >= 300) {
        }
    };
    XHR.send();
}

function getEmailString(emailObjArray) {

    var emailTo = "";
    for (var i = 0; i < emailObjArray.length; i++) {
        emailTo += emailObjArray[i].email;
        if (i != emailObjArray.length - 1) {
            emailTo += ", ";
        }
    }

    return emailTo;
}

function fillEmailsTo(emailTo) {
    var emailInputElement = document.querySelector(".email-frm-to");
    emailInputElement.value = emailTo;
}

function getEmailElementsObjFromEmailFormElements() {

    var emailElem = document.querySelector(".email-frm-to");
    var subjectElem = document.querySelector(".email-frm-subject");
    var textElem = document.querySelector(".email-frm-text");

    return {
        emailElem: emailElem,
        subjectElem: subjectElem,
        templateElem: templateSelector,
        textElem: textElem
    }
}

sendEmailActionBtn.onclick = function () {
    var emailObjElements = getEmailElementsObjFromEmailFormElements();

    var emailArray = [];

    var strArray = emailObjElements.emailElem.value.split(",");
    for (var i = 0; i < strArray.length; i++) {
        emailArray.push(strArray[i].trim());
    }

    var emailObj = {
        idList: getSelectedContactArray(),
        subject: emailObjElements.subjectElem.value,
        template: templateSelector.value,
        text: emailObjElements.textElem.value
    };

    if (emailArray.length > 0) {
        var emailJSON = JSON.stringify(emailObj);
        var method = "POST";
        var url = "http://localhost:8080/directory/main/contacts";

        sendDataAssynchRequest(emailJSON, method, url);
    } else {
        popupNotify("Fulfill field 'To'!", false)
    }
};

function sendDataAssynchRequest(data, method, url) {

    var xhr = new XMLHttpRequest();
    xhr.open(method, url);
    var contentType = "application/json";
    var purpose = 'email';
    xhr.setRequestHeader("Content-type", contentType);
    xhr.setRequestHeader("email", purpose);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                window.history.pushState(null, "home", "home");
                hideEmailElements();
                popupNotify("Email send", true)
            } else if (xhr.status >= 300) {
                popupNotify("Email wasn't send", false);
            }
        }
    };
    xhr.send(data);
}

templateSelector.onchange = function () {

    var emailObj = getEmailElementsObjFromEmailFormElements();

    if (templateSelector.value === "") {

        emailObj.textElem.value = "";
        emailObj.textElem.disabled = false;

    } else {
        //send request to get template
        emailObj.textElem.disabled = true;
        var url = "http://localhost:8080/directory/main/contacts";
        var header = templateSelector.value;
        getTemplate(url, header);
    }

};

function getTemplate(url, header) {
    var xhr = new XMLHttpRequest();

    xhr.open('GET', url);
    xhr.setRequestHeader("template", header);
    xhr.responseType = 'json';
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {

            var template = xhr.response;
            var emailObjElements = getEmailElementsObjFromEmailFormElements();
            emailObjElements.textElem.value = template.text;
        } else if (xhr.readyState === 4 && xhr.status >= 300) {

        }
    };
    xhr.send();
}