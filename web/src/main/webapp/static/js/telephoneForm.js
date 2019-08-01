"use strict";

var telephonesArray = {telephones:[]};
showTelephoneTable(telephonesArray);

function cleanTelephonesFromTable() {
    telephonesArray = {telephones:[]};
    showTelephoneTable(telephonesArray);
    displayTelephoneBtns();
}

var crtTelephoneBtn = document.querySelectorAll(".frm-telephone-btn")[0];
var cancelTelephoneBtn = document.querySelectorAll(".frm-telephone-btn")[2];

//check telephonesArray
//hide telephone's btns if telephones are empty
//show telephone's btns if telephones have at least one telephone
function displayTelephoneBtns() {

    if (telephonesArray.telephones.length > 0) {
        updateTelephoneBtn.style.display = "inline-block";
        deleteTelephoneBtn.style.display = "inline-block";
    } else {
        updateTelephoneBtn.style.display = "none";
        deleteTelephoneBtn.style.display = "none";
    }
}

//add new telephone into table
crtTelephoneBtn.onclick = function () {

    var newTelephone = getTelephoneFromForm();

    if (newTelephone) {

        telephonesArray.telephones.push(newTelephone);
        frmTelephone.style.display = "none";
        showTelephoneTable(telephonesArray);
        displayTelephoneBtns();
    }
};

//output list of telephones in phone table
function showTelephoneTable(telephonesArray) {
    var telephonesTblContainer = document.querySelector(".telephones-tbl-container");
    if (telephonesArray.telephones.length === 0) {
        telephonesTblContainer.innerHTML = "";
    } else {
        console.log(telephonesArray);
        $.get('../templates/telephones.mustache', function (phonesTmpl) {
            var rendered = Mustache.render(phonesTmpl, telephonesArray);
            $('.telephones-tbl-container').html(rendered);
        });
    }
}

function getTelephoneFormElements() {
    var phoneCountry = document.querySelectorAll(".frm-telephone-content")[0];
    var phoneOperator = document.querySelectorAll(".frm-telephone-content")[1];
    var phoneNumber = document.querySelectorAll(".frm-telephone-content")[2];
    var phoneTypeArray = document.querySelectorAll(".frm-telephone-content input[name='phone-type']");
    var phoneComment = document.querySelectorAll(".frm-telephone-content")[4];
    var telephoneFormElements = {
        phoneCountry:phoneCountry,
        phoneOperator:phoneOperator,
        phoneNumber:phoneNumber,
        phoneTypeArray:phoneTypeArray,
        phoneComment:phoneComment
    };
    return telephoneFormElements;
}

function getTelephoneFromForm() {

    var errorArray = [];

    var telephoneFormElements = getTelephoneFormElements();
    var phoneCountry = telephoneFormElements.phoneCountry.value;

    if (phoneCountry) {
        if (isNaN(phoneCountry)) {
            errorArray.push(telephoneFormElements.phoneCountry);
        }
    }

    setTelephoneInputDefault(telephoneFormElements.phoneCountry);

    var phoneOperator = telephoneFormElements.phoneOperator.value;

    if (phoneOperator) {

        if (isNaN(phoneOperator)) {
            errorArray.push(telephoneFormElements.phoneOperator);
        }
    }
    setTelephoneInputDefault(telephoneFormElements.phoneOperator);

    var phoneNumber = telephoneFormElements.phoneNumber.value;

    if (phoneNumber) {

        if (isNaN(phoneNumber)) {
            errorArray.push(telephoneFormElements.phoneNumber);
        }
    }
    setTelephoneInputDefault(telephoneFormElements.phoneNumber);

    var phoneType = null;

    for (var i = 0; i < telephoneFormElements.phoneTypeArray.length; i++) {
        if (telephoneFormElements.phoneTypeArray[i].checked) {
            phoneType = telephoneFormElements.phoneTypeArray[i].value;
        }
    }

    if (errorArray.length > 0) {
        for (var i = 0; i < errorArray.length; i++) {
            setTelephoneInputError(errorArray[i]);
        }
        return;
    }

    var phoneComment = telephoneFormElements.phoneComment.value;

    //set fields as default
    cleanTelephoneFormElemsValues(telephoneFormElements);

    var telephone = {
        country:phoneCountry,
        operator:phoneOperator,
        number:phoneNumber,
        phoneType:phoneType,
        comment:phoneComment
    };
    return telephone;
};

//clean telephone form fields
function cleanTelephoneFormElemsValues(telephoneFormElements) {
    telephoneFormElements.phoneCountry.value = "";
    telephoneFormElements.phoneOperator.value = "";
    telephoneFormElements.phoneNumber.value = "";
    telephoneFormElements.phoneTypeArray[0].checked = "true";
    telephoneFormElements.phoneComment.value = "";
}

//close telephone form
cancelTelephoneBtn.onclick = function () {
    frmTelephone.style.display = "none";
    var telephoneFormElements = getTelephoneFormElements();
    setTelephoneInputDefault(telephoneFormElements.phoneCountry);
    setTelephoneInputDefault(telephoneFormElements.phoneOperator);
    setTelephoneInputDefault(telephoneFormElements.phoneNumber);
    cleanTelephoneFormElemsValues(telephoneFormElements);
};


//delete telephone from table
var deleteTelephoneBtn = document.querySelector(".delete-telephone-btn");

deleteTelephoneBtn.onclick = function() {
    var telephoneLineArray = document.querySelectorAll(".telephone-line");
    if (telephoneLineArray) {
        for (var i = 0, j = 0; i < telephoneLineArray.length; i++, j++) {
            if (telephoneLineArray[i].querySelector("input[type='checkbox']").checked) {
                telephonesArray.telephones.splice(j, 1);
                j--;
            }
        }
        showTelephoneTable(telephonesArray);
        displayTelephoneBtns();
    }
};


//update existed telephone in the table
var updateTelephoneBtn = document.querySelector(".update-telephone-btn");
updateTelephoneBtn.onclick = function () {

    saveTelephoneBtn.style.display = "inline-block";
    crtTelephoneBtn.style.display = "none";

    var telephoneLineArray = document.querySelectorAll(".telephone-line");
    //check if any telephone exists
    var isChecked = false;
    for (var l = 0; l < telephoneLineArray.length; l++) {
        console.log(telephonesArray.telephones[l]);
        console.log(telephoneLineArray[l]);
        if (telephoneLineArray[l].querySelector("input[type='checkbox']").checked) {
            isChecked = true;
        }
    }

    if (telephoneLineArray && isChecked) {
        var indexCheckedTelephonesArray = [];
        var elem = 0;

        for (var i = 0; i < telephoneLineArray.length; i++) {
            console.log(telephonesArray.telephones[i]);
            console.log(telephoneLineArray[i]);

            if (telephoneLineArray[i].querySelector("input[type='checkbox']").checked) {
                indexCheckedTelephonesArray.push(i);
            }
        }
        updateOneMoreTelephone(indexCheckedTelephonesArray, elem);
    }
};

var saveTelephoneBtn = document.querySelectorAll(".frm-telephone-btn")[1];

function updateOneMoreTelephone(indexCheckedTelephonesArray, elem) {
    frmTelephone.style.display = "block";
    var telephoneFormElems = getTelephoneFormElements();
    telephoneFormElems.phoneCountry.value = telephonesArray.telephones[indexCheckedTelephonesArray[elem]].country;
    telephoneFormElems.phoneOperator.value = telephonesArray.telephones[indexCheckedTelephonesArray[elem]].operator;
    telephoneFormElems.phoneNumber.value = telephonesArray.telephones[indexCheckedTelephonesArray[elem]].number;
    for (var j = 0; j < telephoneFormElems.phoneTypeArray.length; j++) {
        if (telephonesArray.telephones[elem].phoneType === telephoneFormElems.phoneTypeArray[j].value) {
            telephoneFormElems.phoneTypeArray[j].checked = "true";
        }
    }
    telephoneFormElems.phoneComment.value = telephonesArray.telephones[indexCheckedTelephonesArray[elem]].comment;

    //when click save read from and update info in the array
    saveTelephoneBtn.onclick = function () {
        var updatedTelephoneFormElems = getTelephoneFormElements();
        telephonesArray.telephones[indexCheckedTelephonesArray[elem]].country = updatedTelephoneFormElems.phoneCountry.value;
        telephonesArray.telephones[indexCheckedTelephonesArray[elem]].operator = updatedTelephoneFormElems.phoneOperator.value;
        telephonesArray.telephones[indexCheckedTelephonesArray[elem]].number = updatedTelephoneFormElems.phoneNumber.value;

        for (var k = 0; k < updatedTelephoneFormElems.phoneTypeArray.length; k++) {
            if (updatedTelephoneFormElems.phoneTypeArray[k].checked) {
                telephonesArray.telephones[indexCheckedTelephonesArray[elem]].phoneType = updatedTelephoneFormElems.phoneTypeArray[k].value;
            }
        }
        telephonesArray.telephones[indexCheckedTelephonesArray[elem]].comment = updatedTelephoneFormElems.phoneComment.value;
        if (indexCheckedTelephonesArray.length - 1 === elem) {
            //if last checked element of array
            frmTelephone.style.display = "none";
            cleanTelephoneFormElemsValues(updatedTelephoneFormElems);
            showTelephoneTable(telephonesArray);
        } else {
            //if more checked elements of array left
            elem++;
            showTelephoneTable(telephonesArray);
            updateOneMoreTelephone(indexCheckedTelephonesArray, elem);
        }
    };
}

//popup window
var frmTelephone = document.querySelector(".frm-telephone");
var createTelephonebtn = document.querySelector(".create-telephone-btn");
var closePopupTelephone = document.querySelector(".close-frm-telephone");

createTelephonebtn.onclick = function () {
    saveTelephoneBtn.style.display = "none";
    crtTelephoneBtn.style.display = "inline-block";
    frmTelephone.style.display = "block";
};

closePopupTelephone.onclick = function () {
    cleanTelephoneFormElemsValues(getTelephoneFormElements());
    frmTelephone.style.display = "none";
};

window.onclick = function (event) {
    if (event.target === frmTelephone) {
        cleanTelephoneFormElemsValues(getTelephoneFormElements());
        frmTelephone.style.display = "none";
    }
};

function setTelephoneInputError(telephoneFormElement) {

    telephoneFormElement.value = '';
    telephoneFormElement.style.border = "1px solid red";
    telephoneFormElement.placeholder = "Only numbers required";
}

function setTelephoneInputDefault(telephoneFormElement) {

    telephoneFormElement.style.border = "1px solid gray";
    telephoneFormElement.placeholder = "";
}
