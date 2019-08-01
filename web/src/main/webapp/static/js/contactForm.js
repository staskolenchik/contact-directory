"use strict";

//get pages block object
function linksLoad(contactsQuantity) {

    var contactsQuantityNumber = parseInt(contactsQuantity);
    var pageCount = contactsQuantityNumber / 10;
    var countLeft = contactsQuantityNumber % 10;
    if (countLeft > 0) {
        pageCount++;
    }
    if (document.location.toString().search("home") >= 0) {
        pageBlock.innerHTML = '';
        for (var i = 1; i <= pageCount; i++) {
            var homePageHref = "http://localhost:8080/directory/main/home?page=" + i;
            pageBlock.innerHTML += "<span><a href='" + homePageHref + "'>" + i + "</a></span>";
        }
    } else if (document.location.toString().search("search") >= 0) {
        /*pageBlock.innerHTML = '';
        for (var i = 1; i <= pageCount; i++) {
            var searchPageHref = "http://localhost:8080/directory/main/search?page=" + i;
            pageBlock.innerHTML += "<span><a href='" + searchPageHref + "'>" + i + "</a></span>";
        }*/
    }
}

//get contacts from server
function getContactsDependingOnUri() {

    var currentQueryString = window.location.search;

    var pageNum = null;

    if (currentQueryString.length === 0) {
        getContactsFromServer(pageNum);
    } else {
        var indexOfPageLetters = currentQueryString.indexOf("page=", 1);
        var pageNumStr = currentQueryString.substring(indexOfPageLetters + 5, currentQueryString.length);
        pageNum = parseInt(pageNumStr);
        getContactsFromServer(pageNum);
    }
}

//get contacts from server at page load
document.getElementsByTagName("body")[0].onload = function() {
    getContactsDependingOnUri();
};

function getContactsFromServerSucceed(response) {
    console.log(response);
    if (response.contacts.length > 0) {
        $.get('../templates/contacts.mustache', function (tmpl) {
            var rendered = Mustache.render(tmpl, response);
            $('.contacts-tbl').html(rendered);
            var contactsQuantity = response.contactsQuantity;
            linksLoad(contactsQuantity);
        });

    } else {
        var contactsTable = document.querySelector(".contacts-tbl");
        contactsTable.innerHTML = "Contact list is empty";
    }
}

function getContactsFromServerFailed() {
    var contactsTable = document.querySelector(".contacts-tbl");
    contactsTable.innerHTML = "";
    popupNotify("Error", false)
}

function getContactsAssynchRequest(method, url) {

    var XHR = new XMLHttpRequest();
    XHR.open(method, url);
    XHR.responseType = 'json';
    XHR.onreadystatechange = function (ev) {
        if (XHR.readyState === 4 && XHR.status === 200) {
            console.log(XHR.response);
            getContactsFromServerSucceed(XHR.response);
        } else if (XHR.readyState === 4 && XHR.status >= 300) {
            getContactsFromServerFailed();
        }
    };
    XHR.send();
}

function getContactsFromServer(pageNum) {

    if (pageNum === null) {

        var url = "http://localhost:8080/directory/main/contacts";
        var method = 'GET';

        getContactsAssynchRequest(method, url);

    } else if (pageNum){

        var urlPageNumber = "http://localhost:8080/directory/main/contacts?page=" + pageNum;
        var methodPageNumber = 'GET';

        getContactsAssynchRequest(methodPageNumber, urlPageNumber);
    }
}

var homePageBtn = document.querySelector(".home-page-btn");

var imagePostBlock = document.querySelector(".post-image-block");
var imagePostElem = document.querySelector(".post-image");
var uploadPostImage = document.querySelector(".post-image-file");

var addPostImgBtn = document.querySelector(".post-image-btn button[name='add']");
var removePostImgBtn = document.querySelector(".post-image-btn button[name='remove']");

//
var imageUpdBlock = document.querySelector(".upd-image-block");
var imageUpdElem = document.querySelector(".upd-image");
var uploadUpdImage = document.querySelector(".upd-image-file");

var addUpdImgBtn = document.querySelector(".upd-image-btn button[name='add']");
var removeUpdImgBtn = document.querySelector(".upd-image-btn button[name='remove']");
//

var createContactBtn = document.querySelector(".create-contact-btn");
var createContactForm = document.querySelector(".create-frm");
var cancelCrCntBtn = document.querySelectorAll(".create-frm-btn")[1];

var navBarBlock = document.querySelector(".contact-btn-block");

var insideCreateContactFormCrtCntctBtn = document.querySelector(".create-frm-btn-block");
var insideUpdateContactFormUpdCntctBtn = document.querySelector(".upd-frm-btn-block");

var contactsTable = document.querySelector(".contacts-tbl");

var searchBlock = document.querySelector(".search-block");

var pageBlock = document.querySelector(".pages-block");

var telephonesContainer = document.querySelector(".telephones-container");

window.onpopstate = function () {
    if (document.location.toString().search("create") >= 0) {
        cleanTelephonesFromTable();
        cleanAttachmentArray();
        cleanAttachmentElems();
        showContactCreationElements();

    } else if (document.location.toString().search("home") >= 0) {
        removeImage(imagePostElem, uploadPostImage);
        removeImage(imageUpdElem, uploadUpdImage);
        cleanTelephonesFromTable();
        hideContactCreationElements();
        hideContactUpdateElements();
        hideEmailElements();


    } else if (document.location.toString().search("update") >= 0) {
        showContactUpdateElements();
        hideContactCreationElements();
    }

};

function showContactCreationElements() {
    navBarBlock.style.display = "none";
    contactsTable.style.display = "none";
    pageBlock.style.display = "none";
    updateFormContainer.style.display = "none";
    insideUpdateContactFormUpdCntctBtn.style.display = "none";
    imagePostBlock.style.display = "block";
    imageUpdBlock.style.dysplay = "none";
    createContactForm.style.display = "block";
    insideCreateContactFormCrtCntctBtn.style.display = "block";
    telephonesContainer.style.display = "block";
    displayAttachmentContainer.style.display = "block";
    hideSearchElems();
}

function hideContactCreationElements() {
    cleanContactCreationForm();
    imagePostBlock.style.display = "none";
    imageUpdBlock.style.dysplay = "none";
    createContactForm.style.display = "none";
    insideCreateContactFormCrtCntctBtn.style.display = "none";
    telephonesContainer.style.display = "none";
    displayAttachmentContainer.style.display = "none";
    updateFormContainer.style.display = "none";
    navBarBlock.style.display = "flex";
    contactsTable.style.display = "block";
    //searchBlock.style.display = "block";
    pageBlock.style.display = "inline-block";
    insideUpdateContactFormUpdCntctBtn.style.display = "none";
}

function getContactCreationForm() {

    var contactCreationForm = {
        firstName: document.querySelector(".create-frm-input input[name='fistName']"),
        lastName: document.querySelector(".create-frm-input input[name='lastName']"),
        patronymic: document.querySelector(".create-frm-input input[name='patronymic']"),
        birthday: document.querySelector(".create-frm-input input[name='birthday']"),
        sexArray: document.querySelectorAll(".create-frm-input input[name='sex']"),
        citizenship: document.querySelector(".create-frm-input input[name='citizenship']"),
        maritalStatus: document.querySelector(".create-frm-input input[name='maritalStatus']"),
        website: document.querySelector(".create-frm-input input[name='website']"),
        email: document.querySelector(".create-frm-input input[name='email']"),
        workplace: document.querySelector(".create-frm-input input[name='workplace']"),
        country: document.querySelector(".create-frm-input input[name='country']"),
        city: document.querySelector(".create-frm-input input[name='city']"),
        street: document.querySelector(".create-frm-input input[name='street']"),
        building: document.querySelector(".create-frm-input input[name='building']"),
        apartment: document.querySelector(".create-frm-input input[name='apartment']"),
        index: document.querySelector(".create-frm-input input[name='index']")
    };
    return contactCreationForm;
}

//clean contact creation form
function cleanContactCreationForm() {
    var contactCreationForm = getContactCreationForm();
    contactCreationForm.firstName.value = "";
    contactCreationForm.lastName.value = "";
    contactCreationForm.patronymic.value = "";
    contactCreationForm.birthday.value = "";
    contactCreationForm.sexArray[0].checked = "true";
    contactCreationForm.citizenship.value = "";
    contactCreationForm.maritalStatus.value = "";
    contactCreationForm.website.value = "";
    contactCreationForm.email.value = "";
    contactCreationForm.workplace.value = "";
    contactCreationForm.country.value = "";
    contactCreationForm.city.value = "";
    contactCreationForm.street.value = "";
    contactCreationForm.building.value = "";
    contactCreationForm.apartment.value = "";
    contactCreationForm.index.value = "";

    setFormFieldDefault(contactCreationForm.firstName);
    setFormFieldDefault(contactCreationForm.lastName);
    setFormFieldDefault(contactCreationForm.birthday);
    setFormFieldDefault(contactCreationForm.email);
    console.log("Clean contact creation form")
}

function setFormFieldWarning(formElem) {
    formElem.value = '';
    formElem.style.border = "1px solid red";

    if (formElem.name === 'birthday') {

        formElem.placeholder = "Please fill this field according to mm/dd/yyyy";
        window.scrollTo({top: 500, behavior: "smooth"});
    } else if (formElem.name === 'email') {

        formElem.placeholder = "Please place email in this field";
        window.scrollTo({top: 600, behavior: "smooth"});
    } else {

        formElem.placeholder = "Please fill this field";
        window.scrollTo({top: 350, behavior: "smooth"});
    }
}

function setFormFieldDefault(formElem) {
    //formElem.style.color = "black";
    formElem.style.border = "1px solid gray";
    if (formElem.name === "firstName") {
        formElem.placeholder = "first name";
    }
    if (formElem.name === "lastName") {
        formElem.placeholder = "last name";
    }
    if (formElem.name === "email") {
        formElem.placeholder = "example@mail.com";
    }
    if (formElem.name === "birthday") {
        formElem.placeholder = "mm/dd/yyyy"
    }
}

//open create contact form
createContactBtn.onclick = function () {
    window.history.pushState(null, "create", "create");
    showContactCreationElements();

};

var sendFormBtn = document.querySelectorAll(".create-frm-btn")[0];

sendFormBtn.onclick = function () {

    var contactCreationForm = getContactCreationForm();

    var invalidInputArray = [];

    var firstName = contactCreationForm.firstName.value;
    var lastName = contactCreationForm.lastName.value;

    if (!firstName) {

        invalidInputArray.push(contactCreationForm.firstName);
    }
    setFormFieldDefault(contactCreationForm.firstName);

    if (!lastName) {

        invalidInputArray.push(contactCreationForm.lastName);
    }
    setFormFieldDefault(contactCreationForm.lastName);

    var patronymic = contactCreationForm.patronymic.value;
    var birthday = contactCreationForm.birthday.value;

    if (birthday) {

        var split = birthday.split("/");
        if (split.length !== 3) {

            invalidInputArray.push(contactCreationForm.birthday);
        } else {
            for (var i = 0; i < split.length; i++) {
                var parseValue = parseInt(split[i]);
                if (parseValue != split[i]) {

                    invalidInputArray.push(contactCreationForm.birthday);
                }
            }
            for (var i = 0; i < split.length - 1; i++) {
                if (split[i].length !== 2) {

                    invalidInputArray.push(contactCreationForm.birthday);
                }
            }
            if (split[2].length !== 4) {

                invalidInputArray.push(contactCreationForm.birthday);
            }
        }
    }
    setFormFieldDefault(contactCreationForm.birthday);

    var sexArray = contactCreationForm.sexArray;

    var sex = null;
    for (var i = 0; i < sexArray.length; i++) {
        if (sexArray[i].checked) {
            sex = sexArray[i].value;
        }
    }

    var citizenship = contactCreationForm.citizenship.value;
    var maritalStatus = contactCreationForm.maritalStatus.value;
    var website = contactCreationForm.website.value;
    var email = contactCreationForm.email.value;
    setFormFieldDefault(contactCreationForm.email);

    if (email.indexOf("@") < 1 && email.length > 0) {

        invalidInputArray.push(contactCreationForm.email);
    }



    if (invalidInputArray.length > 0) {
        for (var i = 0; i < invalidInputArray.length; i++) {
            setFormFieldWarning(invalidInputArray[i]);
        }
        return;
    }

    var workplace = contactCreationForm.workplace.value;
    var country = contactCreationForm.country.value;
    var city = contactCreationForm.city.value;
    var street = contactCreationForm.street.value;
    var building = contactCreationForm.building.value;
    var apartment = contactCreationForm.apartment.value;
    var index = contactCreationForm.index.value;

    var attachmentsToServer = [];
    for (var j = 0; j < attachmentArray.attachments.length; j++) {

        var attachmentToServer = {
            fileName: attachmentArray.attachments[j].fileName,
            fileComment: attachmentArray.attachments[j].fileComment
        };
        attachmentsToServer.push(attachmentToServer);
    }

    var imageToSend;
    if (image.data) {
        imageToSend = image;
    }

    var contact = {
        user: {
            firstName: firstName,
            lastName: lastName,
            patronymic: patronymic,
            birthday: birthday,
            sex: sex,
            citizenship: citizenship,
            maritalStatus: maritalStatus,
            website: website,
            email: email,
            workplace: workplace
        },
        address: {
            country: country,
            city: city,
            street: street,
            building: building,
            apartment: apartment,
            index: index
        },
        telephones: telephonesArray.telephones,
        attachments: attachmentsToServer,
        image: imageToSend
    };
    var contactJSON = JSON.stringify(contact);
    console.log(contactJSON);

    var contentType = "application/json";

    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open('POST', "http://localhost:8080/directory/main/contacts");
    xmlHttp.setRequestHeader("Content-type", contentType);

    xmlHttp.onreadystatechange = function () {
        if (xmlHttp.readyState === 4) {
            if (xmlHttp.status === 200) {
                cleanTelephonesFromTable();
                removeImage(imagePostElem, uploadPostImage);
                cleanContactCreationForm();
                window.history.pushState(null, "home", "home");
                hideContactCreationElements();
                getContactsDependingOnUri();
                sendFiles(attachmentArray);
                cleanAttachmentArray();
                removeImage(imagePostElem, uploadPostImage);
                popupNotify('Saved', true);
            } else if ((xmlHttp.status >= 300 && xmlHttp.status < 409) || (xmlHttp.status > 409)) {
                popupNotify('Error', false);
            } else if (xmlHttp.status === 409) {
                popupNotify('User with this firstname and lastname is already exists', false);
            }
        }

    };

    xmlHttp.send(contactJSON);
};


function sendFiles(attachmentArray) {
    var formData = new FormData();

    var countFilesToSend = 0;
    for (var i = 0; i < attachmentArray.attachments.length; i++) {
        if (attachmentArray.attachments[i].filePropsElem) {
            formData.append(attachmentArray.attachments[i].fileName, attachmentArray.attachments[i].filePropsElem.files[0]);
            countFilesToSend++;
            console.log("amount of files to send =" + countFilesToSend);
        }
    }

    if (countFilesToSend > 0) {
        var xmlHttpReq = new XMLHttpRequest();
        xmlHttpReq.open('POST', 'http://localhost:8080/directory/main/contacts');
        xmlHttpReq.setRequestHeader("accept-charset", "utf-8");
        xmlHttpReq.onreadystatechange = function () {
            if (xmlHttpReq.readyState === 4) {
                if (xmlHttpReq.status === 200){
                    console.log("uploaded file successfully");

                } else if (xmlHttpReq.status >= 300){
                    popupNotify("Files weren't saved", false);
                }

            }
        };
        xmlHttpReq.send(formData);
    }
}

//cancel contact creation and return to home page
cancelCrCntBtn.onclick = function () {
    window.history.pushState(null, "home", "home");
    cleanContactCreationForm();
    removeImage(imagePostElem, uploadPostImage);
    cleanAttachmentArray();
    cleanAttachmentElems();
    cleanTelephonesFromTable();
    hideContactCreationElements();
    hideContactUpdateElements();
};

var updateFormContainer = document.querySelector(".upd-frm-container");

var contactsTable = document.querySelector(".contacts-tbl");

var buttonUpdateSubmit = document.querySelector(".upd-frm-btn-block button[name='update']");

var buttonUpdateCancel = document.querySelector(".upd-frm-btn-block button[name='cancel']");

buttonUpdateCancel.onclick = function() {
    window.history.pushState(null, "home", "home");
    updateFormContainer.style.display = "none";
    contactsTable.style.display = "block";
    navBarBlock.style.display = "flex";
    pageBlock.style.display = "block";
    imageUpdBlock.style.display = "none";
    telephonesContainer.style.display = "none";
    displayAttachmentContainer.style.display = "none";
    insideUpdateContactFormUpdCntctBtn.style.display = "none";
    removeImage(imageUpdElem, uploadUpdImage);
    cleanTelephonesFromTable();
    cleanAttachmentArray();
    cleanAttachmentElems();
};

removePostImgBtn.onclick = function () {
    removeImage(imagePostElem, uploadPostImage);
};

removeUpdImgBtn.onclick = function () {
    removeImage(imageUpdElem, uploadUpdImage);
};

function removeImage(imageElem, uploadImage) {
    imageElem.src = "../static/img/avatar/avatar.jpg";
    uploadImage.value = null;
    cleanImage();
}

function addImageClick(uploadImage) {
    uploadImage.click();
}

imagePostElem.onclick = function() {
    addImageClick(uploadPostImage);
};

imageUpdElem.onclick = function() {
    addImageClick(uploadUpdImage);
};

addPostImgBtn.onclick = function () {
    addImageClick(uploadPostImage);
};

addUpdImgBtn.onclick = function () {
    addImageClick(uploadUpdImage);
};

function setNewAvatar(imageFile, imageElem) {
    imageElem.src = URL.createObjectURL(imageFile);
}

uploadPostImage.onchange = function () {
    cleanImage();
    setNewAvatar(uploadPostImage.files[0], imagePostElem);
    imgIntoString(uploadPostImage.files[0]);
};

uploadUpdImage.onchange = function () {
    cleanImage();
    setNewAvatar(uploadUpdImage.files[0], imageUpdElem);
    imgIntoString(uploadUpdImage.files[0]);
};

function cleanImage() {
    image = {
        id: null,
        contentType: null,
        data: null
    };
}

var image = {
    id: null,
    contentType: null,
    data: null
};

function getImgBlobFromImgObj(image) {

    var imageContentType = image.contentType;
    var imageData = image.data;

    var sliceSize = 512;

    var byteCharacters = atob(imageData);
    var byteArrays = [];

    for (var offset = 0; offset < byteCharacters.length; offset += sliceSize) {
        var slice = byteCharacters.slice(offset, offset + sliceSize);

        var byteNumbers = new Array(slice.length);
        for (var i = 0; i < slice.length; i++) {
            byteNumbers[i] = slice.charCodeAt(i);
        }

        var byteArray = new Uint8Array(byteNumbers);

        byteArrays.push(byteArray);
    }

    var imgBlob = new Blob(byteArrays, {type: imageContentType});

    console.log(imgBlob);
    return imgBlob;
}

function setImgObjVariables(stringImgUrl) {
    var block = stringImgUrl.split(";");
    var imageContentType = block[0].split(":")[1];
    var imageData = stringImgUrl.split(",")[1];
    image.contentType = imageContentType;
    image.data = imageData;
}

function imgIntoString(img) {
    var reader = new FileReader();

    reader.onload = function () {
        var stringImgUrl = reader.result;
        setImgObjVariables(stringImgUrl);
    };

    reader.readAsDataURL(img);
}

function showContactUpdateElements() {
    contactsTable.style.display = "none";
    navBarBlock.style.display = "none";
    pageBlock.style.display = "none";
    imageUpdBlock.style.display = "block";
    updateFormContainer.style.display = "block";
    telephonesContainer.style.display = "block";
    displayAttachmentContainer.style.display = "block";
    insideUpdateContactFormUpdCntctBtn.style.display = "block";
    hideSearchElems();
}

function hideContactUpdateElements() {
    imageUpdBlock.style.display = "none";
    updateFormContainer.style.display = "none";
    telephonesContainer.style.display = "none";
    displayAttachmentContainer.style.display = "none";
    insideUpdateContactFormUpdCntctBtn.style.display = "none";
    contactsTable.style.display = "block";
    navBarBlock.style.display = "flex";
    pageBlock.style.display = "block";
}

//update contact event
function updateContact(id) {

    event.preventDefault();

    cleanAttachmentArray();
    cleanAttachmentElems();

    var req = new XMLHttpRequest();

    req.responseType = "json";

    req.open('GET', 'http://localhost:8080/directory/main/contacts/' + id);

    req.onreadystatechange = function getData() {

        if (req.readyState === 4 && req.status === 200) {

            var contact = req.response;
            setValuesUpdFrm(contact);
            window.history.pushState(null, "update", "update");
            showContactUpdateElements();
        }
    };

    req.send();
}

function getUpdateFormElems() {
    var updateForm = {
        id: document.querySelector(".upd-frm input[name='id']"),
        firstName: document.querySelector(".upd-frm input[name='firstName']"),
        lastName: document.querySelector(".upd-frm input[name='lastName']"),
        patronymic: document.querySelector(".upd-frm input[name='patronymic']"),
        birthday: document.querySelector(".upd-frm input[name='birthday']"),
        sexFormArray: document.querySelectorAll(".upd-frm input[name='sex']"),
        citizenship: document.querySelector(".upd-frm input[name='citizenship']"),
        maritalStatus: document.querySelector(".upd-frm input[name='maritalStatus']"),
        website: document.querySelector(".upd-frm input[name='website']"),
        email:document.querySelector(".upd-frm input[name='email']"),
        workplace: document.querySelector(".upd-frm input[name='workplace']"),
        address: {
            country: document.querySelector(".upd-frm input[name='country']"),
            city: document.querySelector(".upd-frm input[name='city']"),
            street: document.querySelector(".upd-frm input[name='street']"),
            building: document.querySelector(".upd-frm input[name='building']"),
            apartment: document.querySelector(".upd-frm input[name='apartment']"),
            index: document.querySelector(".upd-frm input[name='index']")
        }
    };

    return updateForm;
}

function setValuesUpdFrm(contact) {

    var updateFormElemsObj = getUpdateFormElems();

    updateFormElemsObj.id.value = contact.id;
    updateFormElemsObj.firstName.value = contact.firstName;
    updateFormElemsObj.lastName.value = contact.lastName;

    if (contact.patronymic) {
        updateFormElemsObj.patronymic.value = contact.patronymic;
    } else {
        updateFormElemsObj.patronymic.value = null;
    }
    if (contact.birthday) {
        updateFormElemsObj.birthday.value = contact.birthday;
    } else {
        updateFormElemsObj.birthday.value = null;
    }
    var sexFormArray = updateFormElemsObj.sexFormArray;
    if (contact.sex) {
        for (var i = 0; i < sexFormArray.length; i++) {
            if (contact.sex === sexFormArray[i].value) {
                sexFormArray[i].checked = true;
            }
        }
    } else {
        sexFormArray[0].checked = true;
    }
    if (contact.citizenship) {
        updateFormElemsObj.citizenship.value = contact.citizenship;
    } else {
        updateFormElemsObj.citizenship.value = null;
    }
    if (contact.maritalStatus) {
        updateFormElemsObj.maritalStatus.value = contact.maritalStatus;
    } else {
        updateFormElemsObj.maritalStatus.value = null;
    }
    if (contact.website) {
        updateFormElemsObj.website.value = contact.website;
    } else {
        updateFormElemsObj.website.value = null;
    }
    if (contact.email) {
        updateFormElemsObj.email.value = contact.email;
    } else {
        updateFormElemsObj.email.value = null;
    }
    if (contact.workplace) {
        updateFormElemsObj.workplace.value = contact.workplace;
    } else {
        updateFormElemsObj.workplace.value = null;
    }
    if (contact.address.country) {
        updateFormElemsObj.address.country.value = contact.address.country;
    } else {
        updateFormElemsObj.address.country.value = null;
    }
    if (contact.address.city) {
        updateFormElemsObj.address.city.value = contact.address.city;
    } else {
        updateFormElemsObj.address.city.value = null;
    }
    if (contact.address.street) {
        updateFormElemsObj.address.street.value = contact.address.street;
    } else {
        updateFormElemsObj.address.street.value = null;
    }
    if (contact.address.building) {
        updateFormElemsObj.address.building.value = contact.address.building;
    } else {
        updateFormElemsObj.address.building.value = null;
    }
    if (contact.address.apartment) {
        updateFormElemsObj.address.apartment.value = contact.address.apartment;
    } else {
        updateFormElemsObj.address.apartment.value = null;
    }
    if (contact.address.index) {
        updateFormElemsObj.address.index.value = contact.address.index;
    } else {
        updateFormElemsObj.address.index.value = null;
    }
    if (contact.telephones.length > 0) {
        telephonesArray.telephones = contact.telephones;
        showTelephoneTable(telephonesArray);
        displayTelephoneBtns();
    } else {
        telephonesArray = {telephones:[]};
        showTelephoneTable(telephonesArray);
        displayTelephoneBtns();
    }

    if (contact.attachments.length > 0) {
        console.log(contact.attachments);
        attachmentArray.attachments = contact.attachments;

        var fileDate = null;
        for (var j = 0; j < attachmentArray.attachments.length; j++) {
            var fullDateArray = attachmentArray.attachments[j].fileUpload.split(/[/]/);
            fileDate = new Date(fullDateArray[2], fullDateArray[0] - 1, fullDateArray[1]);
            console.log(fileDate);
            attachmentArray.attachments[j].fileUpload = fileDate;
        }
        showAttachmentTable(attachmentArray);
        displayAttachmentBtns();
    } else {
        attachmentArray = {attachments: []};
        showAttachmentTable(attachmentArray);
        displayAttachmentBtns();
    }


    if (contact.image) {
        image = contact.image;
        var imageBlob = getImgBlobFromImgObj(image);
        setNewAvatar(imageBlob, imageUpdElem);
    }

}

//update contact and send to server
buttonUpdateSubmit.onclick = function () {

    var updateFormElemsObj = getUpdateFormElems();

    var invalidInputArray = [];

    var id = updateFormElemsObj.id.value;

    var firstName = updateFormElemsObj.firstName.value;

    var lastName = updateFormElemsObj.lastName.value;

    if (!firstName) {
        invalidInputArray.push(updateFormElemsObj.firstName);
    }
    setFormFieldDefault(updateFormElemsObj.firstName);

    if (!lastName) {
        invalidInputArray.push(updateFormElemsObj.lastName);
    }
    setFormFieldDefault(updateFormElemsObj.lastName);

    var patronymic = updateFormElemsObj.patronymic.value;
    var birthday = updateFormElemsObj.birthday.value;

    if (birthday) {
        var split = birthday.split("/");
        if (split.length !== 3) {
            invalidInputArray.push(updateFormElemsObj.birthday);
        } else {
            for (var i = 0; i < split.length; i++) {
                if (isNaN(split[i])) {
                    invalidInputArray.push(updateFormElemsObj.birthday);
                }
            }
            for (var i = 0; i < split.length - 1; i++) {
                if (split[i].length !== 2) {
                    invalidInputArray.push(updateFormElemsObj.birthday);
                }
            }
            if (split[2].length !== 4) {
                invalidInputArray.push(updateFormElemsObj.birthday);
            }
        }
    }
    setFormFieldDefault(updateFormElemsObj.birthday);

    var sexArray = updateFormElemsObj.sexFormArray;

    var sex = null;
    for (var i = 0; i < sexArray.length; i++) {
        if (sexArray[i].checked) {
            sex = sexArray[i].value;
        }
    }

    var citizenship = updateFormElemsObj.citizenship.value;
    var maritalStatus = updateFormElemsObj.maritalStatus.value;
    var website = updateFormElemsObj.website.value;
    var email = updateFormElemsObj.email.value;

    if (email) {
        if (email.indexOf("@") < 1 && email.length > 0) {
            invalidInputArray.push(updateFormElemsObj.email);
        }
    }
    setFormFieldDefault(updateFormElemsObj.email);

    var workplace = updateFormElemsObj.workplace.value;
    var country = updateFormElemsObj.address.country.value;
    var city = updateFormElemsObj.address.city.value;
    var street = updateFormElemsObj.address.street.value;
    var building = updateFormElemsObj.address.building.value;
    var apartment = updateFormElemsObj.address.apartment.value;
    var index = updateFormElemsObj.address.index.value;

    if (invalidInputArray.length > 0) {
        for (var i = 0; i < invalidInputArray.length; i++) {
            setFormFieldWarning(invalidInputArray[i]);
        }
        return;
    }

    var attachmentsToServer = [];
    for (var j = 0; j < attachmentArray.attachments.length; j++) {
        var date = null;
        var fileUpload = null;
        if (attachmentArray.attachments[j].fileUpload != null) {
            date = new Date(attachmentArray.attachments[j].fileUpload.getTime());
            fileUpload = "" + (date.getMonth()+1) + "/" + date.getDate() + "/" + date.getFullYear();
        }

        var fileId = null;
        if (attachmentArray.attachments[j].fileId != null) {
            fileId = attachmentArray.attachments[j].fileId;
        }

        var attachmentToServer = {
            fileId: fileId,
            fileName: attachmentArray.attachments[j].fileName,
            fileUpload: fileUpload,
            fileComment: attachmentArray.attachments[j].fileComment
        };
        attachmentsToServer.push(attachmentToServer);
    }

    var imageToSend;
    if (image.id) {

        image.data = null;
        imageToSend = image;
    } else if (image.id === null && image.data) {
        imageToSend = image;
    } else if (image.id === null && image.data === null) {
        imageToSend = image;
    }

    var contact = {
        firstName: firstName,
        lastName : lastName,
        patronymic: patronymic,
        birthday: birthday,
        sex : sex,
        citizenship : citizenship,
        maritalStatus : maritalStatus,
        website : website,
        email : email,
        workplace : workplace,
        address : {
            country : country,
            city : city,
            street : street,
            building : building,
            apartment : apartment,
            index : index
        },
        telephones:telephonesArray.telephones,
        attachments:attachmentsToServer,
        image: imageToSend
    };
    console.log(contact);

    var contactJson = JSON.stringify(contact);
    console.log(id);
    console.log(contactJson);
    var xmlHttp = new XMLHttpRequest();
    var url = "http://localhost:8080/directory/main/contacts/" + id;
    xmlHttp.open('PUT', url);
    xmlHttp.setRequestHeader("Content-type", "application/json");
    xmlHttp.onreadystatechange = function sendUpdateFrm() {
        if (xmlHttp.readyState === 4) {

            if (xmlHttp.status === 200) {

                hideContactUpdateElements();
                window.history.pushState(null, "home", "home");
                getContactsDependingOnUri();
                sendFiles(attachmentArray);
                removeImage(imageUpdElem, uploadUpdImage);
                cleanAttachmentArray();
                cleanAttachmentElems();
                cleanTelephonesFromTable();
                popupNotify('Saved', true);

            } else if (xmlHttp.status === 409) {

                popupNotify('User with this firstname and lastname is already exists', false);

            } else if (xmlHttp.status >= 300 && xmlHttp.status !== 409) {

                popupNotify('Error', false);

            }

        }
    };
    xmlHttp.send(contactJson);
};

//delete contact
var deleteContactBtn = document.querySelector(".delete-contact-btn");

function getSelectedContactArray() {
    var idArray = [];
    var checkId = document.querySelectorAll(".contact-check input[type='checkbox']");
    for (var i = 0; i < checkId.length; i++) {
        if (checkId[i].checked) {
            idArray.push(checkId[i].name);
        }
    }
    return idArray;
};

deleteContactBtn.onclick = function () {

    var selectedContactArray = getSelectedContactArray();
    var countRequests = selectedContactArray.length;

    if (countRequests > 0) {
        for (var j = 0; j < countRequests; j++) {
            var deleteRequest = new XMLHttpRequest();
            var id = selectedContactArray[j];
            console.log("current id is =" + id);
            deleteRequest.open('DELETE', 'http://localhost:8080/directory/main/contacts/' + id, false);
            deleteRequest.onreadystatechange = function deleteData() {
                if (deleteRequest.readyState === 4) {
                    if (deleteRequest.status === 200) {
                        console.log("Delete req with id = " + id + " is sent!");
                        popupNotify('Deleted', true);
                    } else if (deleteRequest.status >= 300) {
                        popupNotify('Error', false);
                    }

                }
            };
            deleteRequest.send();
        }
        getContactsDependingOnUri();
    }
};

homePageBtn.onclick = function () {
    window.history.pushState(null, "home", "home");
    hideContactCreationElements();
    hideContactUpdateElements();
    hideSearchElems();
    getContactsDependingOnUri();
};

function stopPopupNotify() {
    var popupWindow = document.querySelector(".resp-popup");
    var popupMessage = document.querySelector(".resp-popup__message");
    popupWindow.style.display = "none";
    popupMessage.value = "";
}

function popupNotify(message, isSucceed) {

    var popupWindow = document.querySelector(".resp-popup");
    var popupMessage = document.querySelector(".resp-popup__message");

    popupMessage.textContent = message;

    if (isSucceed === true) {
        popupWindow.style.backgroundColor = "lightgreen";
    } else {
        popupWindow.style.backgroundColor = "lightcoral";
    }

    popupWindow.style.display = "flex";

    hideNotification(popupWindow, popupMessage);
}

function hideNotification(popupWindow, popupMessage) {

    var opacity = 1;
    var timer = setInterval(function () {
        opacity -= 0.01;
        popupWindow.style.opacity = opacity;
    }, 20);
    setTimeout(function() {
        popupWindow.style.display = "none";
        popupMessage.value = "";
        clearInterval(timer);
    }, 2000);
}
