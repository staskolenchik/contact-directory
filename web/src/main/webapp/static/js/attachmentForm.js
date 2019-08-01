"use strict";

var attachmentArray = {attachments:[]};
showAttachmentTable(attachmentArray);

function cleanAttachmentArray() {
    attachmentArray = {attachments:[]};
    showAttachmentTable(attachmentArray);
    displayAttachmentBtns();
}

function displayAttachmentBtns() {
    if (attachmentArray.attachments.length > 0) {
        updateAttachmentBtn.style.display = "inline-block";
        deleteAttachmentBtn.style.display = "inline-block";
    } else {
        updateAttachmentBtn.style.display = "none";
        deleteAttachmentBtn.style.display = "none";
    }
}

var displayAttachmentContainer = document.querySelector(".attachment-container");
var attachmentForm = document.querySelector(".frm-attachment");
var createAttachmentBtn = document.querySelector(".create-attachment-btn");

var closePopupAttachment = document.querySelector(".close-frm-attachment");
var addAttachmentIntoAttachmentArrayBtn = document.querySelectorAll(".frm-attachment-btn")[0];
var updateAttachmentInAttachmentArrayBtn = document.querySelectorAll(".frm-attachment-btn")[1];
var cancelBtnPopupAttachment = document.querySelectorAll(".frm-attachment-btn")[2];


createAttachmentBtn.onclick = function () {

    addAttachmentIntoAttachmentArrayBtn.style.display = "inline-block";
    updateAttachmentInAttachmentArrayBtn.style.display = "none";
    attachmentForm.style.display = "block";

    addAttachmentIntoAttachmentArrayBtn.onclick = function addAttachmentIntoAttachmentArray() {
        debugger;
        updateAttachmentInAttachmentArrayBtn.style.display = "none";

        var filePropsElem = document.querySelectorAll(".frm-attachment-content")[0];
        var fileName = filePropsElem.files[0].name;
        var fileValue = filePropsElem.value;

        //attachment comment
        var fileCommentElem = document.querySelectorAll(".frm-attachment-content")[1];
        var fileComment = fileCommentElem.value;

        var attachment = {
            filePropsElem: filePropsElem,
            fileCommentElem: fileCommentElem,
            fileName: fileName,
            fileValue: fileValue,
            fileComment: fileComment
        };

        attachmentArray.attachments.push(attachment);
        showAttachmentTable(attachmentArray);
        displayAttachmentBtns();
        hidePopupAttachment();
        cleanAttachmentElems();
    };
};

function cleanAttachmentElems() {
    var frmAttachmentFields = document.querySelector(".frm-attachment-fields");
    frmAttachmentFields.innerHTML = "";
    frmAttachmentFields.innerHTML += "<div>\n" +
        "                        <label for=\"filename\">File : </label>\n" +
        "                        <input id=\"filename\" class=\"frm-attachment-content\" type=\"file\">\n" +
        "                    </div>\n" +
        "                    <div>\n" +
        "                        <label for=\"fileComment\">Comment : </label>\n" +
        "                        <input id=\"fileComment\" class=\"frm-attachment-content\" type=\"text\">\n" +
        "                    </div>";
}

function showPopupAttachment() {
    attachmentForm.style.display = "block";
}

function hidePopupAttachment() {
    attachmentForm.style.display = "none";
    cleanAttachmentElems()
}

cancelBtnPopupAttachment.onclick = function () {
    hidePopupAttachment();
};

closePopupAttachment.onclick = function () {
    hidePopupAttachment();
};

window.onclick = function (event) {
    if (event.target === attachmentForm) {
        hidePopupAttachment();
    }
};

function showAttachmentTable(attachmentArray) {
    var attachmentsTblContainer = document.querySelector(".attachments-tbl-container");
    if (attachmentArray.attachments.length === 0) {

        attachmentsTblContainer.innerHTML = "";
    } else {

        for (var i = 0; i < attachmentArray.attachments.length; i++) {
            console.log(attachmentArray);
            $.get('../templates/attachments.mustache', function (attachTmpl) {
                var rendered = Mustache.render(attachTmpl, attachmentArray);
                $('.attachments-tbl-container').html(rendered);
            });
        }
    }
}

var updateAttachmentBtn = document.querySelector(".update-attachment-btn");
var deleteAttachmentBtn = document.querySelector(".delete-attachment-btn");

//delete attachment from table
deleteAttachmentBtn.onclick = function() {
    var AttachmentLineArray = document.querySelectorAll(".attachment-line");
    if (AttachmentLineArray) {
        for (var i = 0, j = 0; i < AttachmentLineArray.length; i++, j++) {
            if (AttachmentLineArray[i].querySelector("input[type='checkbox']").checked) {
                attachmentArray.attachments.splice(j, 1);
                j--;
            }
        }
        showAttachmentTable(attachmentArray);
        displayAttachmentBtns();
    }
};

//update attachment from table
updateAttachmentBtn.onclick = function () {

    var attachmentLineArray = document.querySelectorAll(".attachment-line");
    //check if any attachment exists
    var isChecked = false;
    for (var l = 0; l < attachmentLineArray.length; l++) {

        if (attachmentLineArray[l].querySelector("input[type='checkbox']").checked) {
            isChecked = true;
        }
    }

    if (attachmentLineArray && isChecked) {
        var indexCheckedAttachmentArray = [];
        var elem = 0;

        for (var i = 0; i < attachmentLineArray.length; i++) {
            console.log(attachmentArray.attachments[i]);
            console.log(attachmentLineArray[i]);

            if (attachmentLineArray[i].querySelector("input[type='checkbox']").checked) {
                indexCheckedAttachmentArray.push(i);
            }
        }

        console.log(indexCheckedAttachmentArray);
        console.log(elem);
        updateAttachmentForm(indexCheckedAttachmentArray, elem);
    }
};



function updateAttachmentForm(indexCheckedAttachmentArray, elem) {

    var fileElem = document.querySelectorAll(".frm-attachment-content")[0];

    document.querySelectorAll(".frm-attachment-content")[1].value =
        attachmentArray.attachments[indexCheckedAttachmentArray[elem]].fileCommentElem.value;

    addAttachmentIntoAttachmentArrayBtn.style.display = "none";
    updateAttachmentInAttachmentArrayBtn.style.display = "inline-block";

    //display attachment popup form
    showPopupAttachment();
    showAttachmentTable(attachmentArray);

    //when click save read from form and update info in the array
    updateAttachmentInAttachmentArrayBtn.onclick = function () {
        if (fileElem.files.length === 0) {
            updateAttachmentForm(indexCheckedAttachmentArray, elem);
        } else if (indexCheckedAttachmentArray.length - 1 === elem) {
            updateAttachmentAttachArrayFunc(indexCheckedAttachmentArray, elem);
            hidePopupAttachment();
            showAttachmentTable(attachmentArray);
            cleanAttachmentElems();
        } else {
            elem++;
            updateAttachmentAttachArrayFunc(indexCheckedAttachmentArray, elem);
            showAttachmentTable(attachmentArray);
            cleanAttachmentElems();
            updateAttachmentForm(indexCheckedAttachmentArray, elem);
        }
    }
}

function updateAttachmentAttachArrayFunc(indexCheckedAttachmentArray, elem) {

    var filePropsElem = document.querySelectorAll(".frm-attachment-content")[0];
    var fileUpload = new Date();
    var fileName = filePropsElem.files[0].name;
    var fileSize = filePropsElem.files[0].size;
    var fileType = filePropsElem.files[0].type;
    var fileValue = filePropsElem.value;

    //attachment comment
    var fileCommentElem = document.querySelectorAll(".frm-attachment-content")[1];
    var fileComment = fileCommentElem.value;

    var attachment = {
        filePropsElem: filePropsElem,
        fileCommentElem: fileCommentElem,
        fileUpload: fileUpload,
        fileName: fileName,
        fileValue: fileValue,
        fileComment: fileComment
    };

    attachmentArray.attachments[indexCheckedAttachmentArray[elem]] = attachment;
}

function downloadFile(fileName) {
    event.preventDefault();

    var xhr = new XMLHttpRequest();

    var method = "GET";
    var url = "http://localhost:8080/directory/main/contacts";
    var header = fileName;
    xhr.responseType = 'blob';

    xhr.open(method, url);
    xhr.setRequestHeader("attachment", header);

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            console.log(xhr.response);
            var blob = xhr.response;
            var contentDisposition = xhr.getResponseHeader('Content-Disposition');
            var fileName = contentDisposition.match(/filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/)[1];

            console.log(contentDisposition);
            saveBlob(blob, fileName)
        } else if (xhr.readyState === 4 && xhr.status > 300){
            alert("file downloadFile error");
        }
    };
    xhr.send();
}

function saveBlob(blob, fileName) {
    var a = document.createElement('a');
    a.href = window.URL.createObjectURL(blob);
    a.download = fileName;
    a.dispatchEvent(new MouseEvent('click'));
}
