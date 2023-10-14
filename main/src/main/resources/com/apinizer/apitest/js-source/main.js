let mainNavTabs = document.querySelectorAll("#main-nav > div");
const mainNav = document.getElementById("main-nav");
const mainNavAddBtn = document.getElementById("add-tab-btn");

const mainBody = document.getElementById("main-body");
const blankBody = document.getElementById("blank-body");

const urlInput = document.getElementById("url-input");
const statusCode = document.getElementById("response-status-code");
const responseTime = document.getElementById("response-time");
const responseSize = document.getElementById("response-size");

const methodBtn = document.getElementById("method-button");
const methodContainer = document.getElementById("method-container")

const tabName = document.getElementById("tab-name");
const editNameButton = document.querySelector("#tab-name-container img");
const editNameInput = document.querySelector("#tab-name-container input");

let temporalList = [];

const methodColors = {
    "GET": "rgba(107, 149, 255, 1)",
    "POST": "rgba(161, 255, 107, 1)",
    "PUT": "rgba(255, 193, 107, 1)",
    "HEAD": "rgba(74, 72, 69, 0.50)",
    "OPTIONS": "rgba(106, 197, 231, 1)",
    "DELETE": "rgba(231, 106, 112, 1)",
    "PATCH": "rgba(18, 3, 4, 0.85)",
    "TRACE": "rgba(74, 74, 74, 0.50)",
}


let activeElementId;

if(mainNav.children.length === 1){
    mainBody.classList.add("empty");
    mainNavAddBtn.style.marginBottom = "4px";
    mainNavAddBtn.style.marginTop = "4px";
    blankBody.style.display = "flex";
}

function renameTab(id, name){
    mainNavTabs = document.querySelectorAll("#main-nav > div");
    for(let i = 0; i < mainNavTabs.length; i++){
        if(id === mainNavTabs[i].dataset.id){
            mainNavTabs[i].firstChild.textContent = name;
            javaFXController.renameTab(id, name);
            tabList[id].name = name;
            if(activeElementId === id){
                tabName.textContent = name;
            }
            break;
        }
    }
}



function changeTab(id){
    try{
        mainNavTabs = document.querySelectorAll("#main-nav > div");
        mainNavTabs.forEach((div) => {
            if(div.dataset.id === id.toString()){
                div.classList.add("active");
                return;
            }
            if(div.id !== "add-tab-btn")div.classList.remove("active")
        } );
        if(activeElementId){
            tabList[activeElementId].method = methodBtn.dataset.method;
            tabList[activeElementId].url = urlInput.value;
            let child;
            tabList[activeElementId].parameters = [];
            while(root.firstElementChild){
                child = root.firstElementChild;
                tabList[activeElementId].parameters.push([child.children[0].value, child.children[1].value]);
                root.removeChild(root.firstElementChild);
            }
            tabList[activeElementId].headers = [];
            while(headerRoot.firstElementChild){
                child = headerRoot.firstElementChild;
                tabList[activeElementId].headers.push([child.children[0].children[0].value, child.children[1].children[0].value]);
                headerRoot.removeChild(headerRoot.firstElementChild);
            }
            tabList[activeElementId].urlencoded = [];
            while(encodedRoot.firstElementChild){
                child = encodedRoot.firstElementChild;
                tabList[activeElementId].urlencoded.push([child.children[0].value, child.children[1].value]);
                encodedRoot.removeChild(encodedRoot.firstElementChild);
            }

            tabList[activeElementId].authMethod = typeSelector.firstElementChild.textContent.trim();
            switch (tabList[activeElementId].authMethod){
                case "API Key":
                    tabList[activeElementId].authKey = inputField.firstElementChild.children[0].children[1].value;
                    tabList[activeElementId].authValue = inputField.firstElementChild.children[1].children[1].value;
                    break;
                case "Bearer Token":
                    tabList[activeElementId].authToken = inputField.firstElementChild.children[0].children[1].value;
                    break;
                case "Basic Auth":
                    tabList[activeElementId].authUsername = inputField.firstElementChild.children[0].children[1].value;
                    tabList[activeElementId].authPassword = inputField.firstElementChild.children[1].children[1].value;
                    break;
            }
            tabList[activeElementId].body = requestEditor.getValue();
            tabList[activeElementId].responseBody = responseEditor.getValue();

            tabList[activeElementId].responseHeaders = [];
            while(responseHeaderContainer.firstElementChild){
                child = responseHeaderContainer.firstElementChild;
                tabList[activeElementId].responseHeaders.push([child.children[0].textContent, child.children[1].textContent]);
                responseHeaderContainer.removeChild(responseHeaderContainer.firstElementChild);
            }

            tabList[activeElementId].statusCode = statusCode.firstElementChild.textContent;
            tabList[activeElementId].responseTime = responseTime.firstElementChild.textContent;
            tabList[activeElementId].responseSize = responseSize.firstElementChild.textContent;
        }

        activeElementId = id;
        methodBtn.dataset.method = tabList[activeElementId].method;
        methodBtn.children[0].textContent = tabList[activeElementId].method;
        methodBtn.children[0].style.color = methodColors[tabList[activeElementId].method];
        urlInput.value = tabList[activeElementId].url;
        let temp;
        if(tabList[activeElementId].parameters){
            for(let i = 0; i < tabList[activeElementId].parameters.length; i++){
                temp = tabList[activeElementId].parameters[i];
                addNewParam(temp[0], temp[1]);
            }
        }
        if(tabList[activeElementId].headers) {
            for (let i = 0; i < tabList[activeElementId].headers.length; i++) {
                temp = tabList[activeElementId].headers[i]
                addNewHeader(temp[0], temp[1]);
            }
        }
        if(tabList[activeElementId].urlencoded){
            for(let i = 0; i < tabList[activeElementId].urlencoded.length; i++){
                temp = tabList[activeElementId].urlencoded[i];
                addUrlEncodedBody(temp[0], temp[1], "");
            }
        }

        javaFXController.info(tabList[activeElementId].authMethod)
        changeAuthEvent(tabList[activeElementId].authMethod);

        typeSelector.firstElementChild.textContent = tabList[activeElementId].authMethod;
        switch (tabList[activeElementId].authMethod){
            case "API Key":
                inputField.firstElementChild.children[0].children[1].value = tabList[activeElementId].authKey;
                inputField.firstElementChild.children[1].children[1].value = tabList[activeElementId].authValue;
                break;
            case "Bearer Token":
                inputField.firstElementChild.children[0].children[1].value = tabList[activeElementId].authToken;
                break;
            case "Basic Auth":
                inputField.firstElementChild.children[0].children[1].value = tabList[activeElementId].authUsername;
                inputField.firstElementChild.children[1].children[1].value = tabList[activeElementId].authPassword;
                break;
        }

        requestEditor.setValue(tabList[activeElementId].body);
        responseEditor.setValue(tabList[activeElementId].responseBody)

        if(tabList[activeElementId].responseHeaders){
            for(let i = 0; i < tabList[activeElementId].responseHeaders.length; i++){
                temp = tabList[activeElementId].responseHeaders[i];
                createHeaderContainer(temp[0], temp[1]);
            }
        }

        statusCode.firstElementChild.textContent = tabList[activeElementId].statusCode;
        responseTime.firstElementChild.textContent = tabList[activeElementId].responseTime;
        responseSize.firstElementChild.textContent = tabList[activeElementId].responseSize;

        if(tabList[activeElementId].statusCode)response.classList.add("active-z");
        else response.classList.remove("active-z");

        tabName.textContent = tabList[activeElementId].name.substring(0,40);

    }catch (ex){
        if(ex instanceof TypeError){
            return;
        }
        javaFXController.info(JSON.stringify(tabList));
        javaFXController.info(activeElementId);
        javaFXController.debug(ex);
    }
}

function saveChanges(){
    try{
        if(!activeElementId)return;
        tabList[activeElementId].method = methodBtn.dataset.method;
        tabList[activeElementId].url = urlInput.value;
        let child;
        tabList[activeElementId].parameters = [];
        for(let i = 0; i < root.children.length; i++){
            child = root.children[i];
            tabList[activeElementId].parameters.push([child.children[0].value, child.children[1].value]);
        }
        tabList[activeElementId].headers = [];
        for(let i = 0; i < headerRoot.children.length; i++){
            child = headerRoot.children[i];
            tabList[activeElementId].headers.push([child.children[0].children[0].value, child.children[1].children[0].value]);
        }
        tabList[activeElementId].urlencoded = [];
        for(let i = 0; i < encodedRoot.children.length; i++){
            child = encodedRoot.children[i];
            tabList[activeElementId].urlencoded.push([child.children[0].value, child.children[1].value]);
        }
        tabList[activeElementId].authMethod = typeSelector.firstElementChild.textContent.trim();
        switch (tabList[activeElementId].authMethod){
            case "API Key":
                tabList[activeElementId].authKey = inputField.firstElementChild.children[0].children[1].value;
                tabList[activeElementId].authValue = inputField.firstElementChild.children[1].children[1].value;
                break;
            case "Bearer Token":
                tabList[activeElementId].authToken = inputField.firstElementChild.children[0].children[1].value;
                break;
            case "Basic Auth":
                tabList[activeElementId].authUsername = inputField.firstElementChild.children[0].children[1].value;
                tabList[activeElementId].authPassword = inputField.firstElementChild.children[1].children[1].value;
                break;
        }

        tabList[activeElementId].body = requestEditor.getValue();
        tabList[activeElementId].responseBody = responseEditor.getValue();

        tabList[activeElementId].responseHeaders = [];
        for(let i = 0; i < responseHeaderContainer.children.length; i++){
            child = responseHeaderContainer.children[i];
            tabList[activeElementId].responseHeaders.push([child.children[0].textContent, child.children[1].textContent]);
        }

        tabList[activeElementId].statusCode = statusCode.firstElementChild.textContent;
        tabList[activeElementId].responseTime = responseTime.firstElementChild.textContent;
        tabList[activeElementId].responseSize = responseSize.firstElementChild.textContent;
    }catch (error){
        javaFXController.debug(error);
    }
}


mainNavAddBtn.addEventListener("click", (e) => {
    addNewTab("New Request");
})

function addNewTab(name, id){
    const element = document.createElement("div");
    element.dataset.id = id ? id : javaFXController.addNewTab();
    const firstChild = document.createElement("span");
    firstChild.textContent = name ? name.substring(0,40) : "New Request";
    firstChild.classList.add("main-nav-label");
    element.append(firstChild)
    const secondChild = document.createElement("span");
    secondChild.classList.add("nav-close-btn");
    secondChild.innerHTML = "<img style=\"object-fit: cover; max-width: 1rem;margin-bottom: -1px; border-radius: 4px; padding: 2px;\" src=\"../images/9104213_close_cross_remove_delete_icon.png\" alt=\"remove-btn\">";
    element.append(secondChild)
    element.append(document.createElement("div"));

    addButtonEvents(element);
    mainNavTabs = document.querySelectorAll("#main-nav > div");
    mainNavTabs.forEach((div) => {if(div.id !== "add-tab-btn")div.classList.remove("active")} );
    element.classList.add("active");
    changeTab(element.dataset.id);

    tabName.textContent = name.substring(0,40);

    mainNav.insertBefore(element, mainNavAddBtn);
    mainBody.classList.remove("empty");
    mainNavAddBtn.style.marginBottom = "0";
    mainNavAddBtn.style.marginTop = "0";
    blankBody.style.display = "none";

}

function addButtonEvents(element){
    let tabId = element.dataset.id
    element.addEventListener("click", (e) => {
        mainNavTabs = document.querySelectorAll("#main-nav > div");
        mainNavTabs.forEach((div) => {if(div.id != "add-tab-btn"){div.classList.remove("active")}} );
        element.classList.add("active");
        changeTab(element.dataset.id);
    })
    element.addEventListener("mouseover", () => {
        element.children[1].style.opacity = "1";
    });

    element.addEventListener("mouseout", () => {
        element.children[1].style.opacity = "0";
    });

    element.children[1].addEventListener("click", (e) => {
        e.stopPropagation();
       deleteTab(element);
    });
}

function deleteTab(element){
    let tabId = element.dataset.id
    mainNav.removeChild(element);
    if(element.classList.contains("active") && mainNav.children.length !== 1){
        mainNav.children[0].classList.add("active");
        changeTab(mainNav.children[0].dataset.id)
    }
    javaFXController.deleteTab(mainNav.children.length === 1 ? null : activeElementId, element.dataset.id)

    if(mainNav.childElementCount === 1){
        mainBody.classList.add("empty");
        mainNavAddBtn.style.marginBottom = "4px";
        mainNavAddBtn.style.marginTop = "4px";
        blankBody.style.display = "flex";
        activeElementId = undefined;
    }
    if(Object.keys(tabList).includes(tabId)){
        delete tabList[tabId];
    }
    javaFXController.info(tabList[tabId]);
}



editNameButton.addEventListener("click", () => {
    editNameInput.style.display = "inline-block";
    editNameInput.focus();
})

window.addEventListener("click", (e) => {
    if(e.target !== editNameInput && e.target !== editNameButton){
        editNameInput.value = "";
        editNameInput.style.display = "none";
    }
})
function isWhitespaceString(str) {
    // Use a regular expression to check for whitespace characters
    return /^\s*$/.test(str);
}
editNameInput.addEventListener("keydown", (e) => {
    try{
        javaFXController.info(e.keyCode)
        if(e.keyCode === 13 || e.keyCode === 27){
            if(e.keyCode === 13 && !isWhitespaceString(editNameInput.value) && editNameInput.value !== "") {
                mainNavTabs = document.querySelectorAll("#main-nav > div");
                for (let i = 0; i < mainNavTabs.length; i++) {
                    if (activeElementId === mainNavTabs[i].dataset.id) {
                        mainNavTabs[i].firstChild.textContent = editNameInput.value;
                        tabName.textContent = editNameInput.value.substring(0,40);
                        javaFXController.renameTab(activeElementId, editNameInput.value);
                        tabList[activeElementId].name = editNameInput.value;
                        break;
                    }
                }
            }
            editNameInput.value = "";
            editNameInput.style.display = "none";
        }
    }catch (exception){
        javaFXController.debug(exception);
    }
})

methodBtn.addEventListener("click", () => {
    methodContainer.classList.toggle("active-flex");
})

window.addEventListener("click" , (e) => {
    if(e.target != methodBtn && !methodContainer.contains(e.target) && !methodBtn.contains(e.target)){
        methodContainer.classList.remove("active-flex");
    }
})

const methods = document.querySelectorAll("#method-container > button");



methods.forEach(item => {
    item.children[0].style.color = methodColors[item.children[0].dataset.value];
    item.addEventListener("click", () => {
    methodBtn.dataset.method = item.children[0].dataset.value;
    methodBtn.children[0].style.color = methodColors[item.children[0].dataset.value];
    methodBtn.children[0].textContent = item.children[0].textContent;
    methodContainer.classList.remove("active-flex");
    tabList[activeElementId].method = item.children[0].textContent;
    tabList[activeElementId].methodColor = item.children[0].dataset.color;


})});

urlInput.addEventListener("input", (e) => {
    tabList[activeElementId].url = e.target.value;
})

const response = document.getElementById("response");

const settingsBtn = document.getElementById("settings-btn");
const sendBtn = document.getElementById("send-btn");
const saveBtn = document.getElementById("save-btn");

settingsBtn.addEventListener("click", () => {
    javaFXController.displayRequestSettings();
})

saveBtn.addEventListener("click", () => {
    try{
        saveChanges();
        javaFXController.saveTab(activeElementId, tabList[activeElementId]);
    }catch(ex){
        javaFXController.debug(ex);
    }
})
let sending = false;
let timeout;
sendBtn.addEventListener("click", (event) => {
    if(sending){
        clearTimeout(timeout);
        sendBtn.textContent = "Send";
        sending = false;
    }else{
        sending = true;
        event.target.textContent = "Cancel";
        timeout = setTimeout(() => {
            sendRequestFunc()
                .then(() => {
                    sendBtn.textContent = "Send"
                    sending = false;
                })
                .catch(error => {
                    javaFXController.debug(error)
                    sendBtn.textContent = "Send"
                    sending = false;
                });
        }, 1000);
    }
})

async function sendRequestFunc(){
    try{
        const headers = document.getElementsByClassName("header-input-element-container");
        const urlencodeds = document.getElementsByClassName("urlencoded-input-element-container");
        let input1;
        let input2;
        let val1;
        let val2;
        //Auth Button

        javaFXController.clearHeaders();

        for(let e of headers){
            input1 = e.children[0].firstElementChild;
            input2 = e.children[1].firstElementChild;
            javaFXController.createBasicHeader(input1.value, input2.value);
        }

        // for(let e of urlencodeds){
        //     input1 = e.children[0].firstElementChild;
        //     input2 = e.children[1].firstElementChild;
        //     javaFXController.createBasicHeader(input1.value, input2.value);
        // }

        if(typeSelector.firstElementChild.textContent.trim() === "Basic Auth"){
            val1 = inputField.firstElementChild.children[0].children[1].value;
            val2 = inputField.firstElementChild.children[1].children[1].value;
            javaFXController.createBasicHeader("Authorization", btoa(val1 + ":" + val2));
        }
        else if(typeSelector.firstElementChild.textContent.trim() === "Bearer Token"){
            val1 = inputField.firstElementChild.children[0].children[1].value;
            javaFXController.createBasicHeader("Authorization", "Bearer " + val1);
        }
        else if(typeSelector.firstElementChild.textContent.trim() === "API Key"){
            val1 = inputField.firstElementChild.children[0].children[1].value;
            val2 = inputField.firstElementChild.children[1].children[1].value;
            javaFXController.createBasicHeader(val1, val2);
        }else{
            javaFXController.createBasicHeader("Authorization", "");
        }

        while(responseHeaderContainer.firstChild){
            responseHeaderContainer.removeChild(responseHeaderContainer.firstChild);
        }
        javaFXController.sendJsRequestEvent(urlInput.value, requestEditor.getValue(), methodBtn.children[0].textContent.toUpperCase());
        Object.entries(responseJavaObject.headers).forEach(([key, value]) => {
            createHeaderContainer(key, value);
        })

        let statusCodeColor = "#00dc00";
        let statCodeFirstDigit = Math.floor(responseJavaObject.statusCode / 100);
        switch (statCodeFirstDigit){
            case 1:
                statusCodeColor = "#3e7bfc";
                break;
            case 2:
                break;
            case 3:
                statusCodeColor = "#dc7d00";
                break;
            case 4:
                statusCodeColor = "red";
                break;
            default:
                statusCodeColor = "red";
        }

        statusCode.firstElementChild.textContent = responseJavaObject.statusCode;
        statusCode.firstElementChild.style.color = statusCodeColor;

        responseTime.firstElementChild.textContent = responseJavaObject.time;
        responseSize.firstElementChild.textContent = responseJavaObject.size;

        responseEditor.setValue(responseJavaObject.data);


        response.classList.add("active-z");


    }catch (error){
        clearTimeout(timeout);
        //javaFXController.debug(error);
    }
}

const mainDiv = document.getElementById("main-div");
const tabButtons = document.querySelectorAll("#tabs button");

const allSections = document.querySelectorAll("#main-div > div");


tabButtons.forEach(button => button.addEventListener("click", ()=> {
    let response = document.getElementById("response");
    response.classList.remove("inside-body");
    for(let element of allSections){
        if(element.id === "body"){
            element.classList.remove("active-z");
        }
        element.style.display = "none";
    }

    for(let singleButton of tabButtons){
        singleButton.classList.remove("active");       
    }

    switch(button.textContent){
        case "Parameters":
            document.getElementById("parameters").style.display = "block";
            break;
        case "Headers":
            document.getElementById("headers").style.display = "block";
            break;
        case "Authorization":
            document.getElementById("authorization").style.display = "flex";
            break;
        case "Body":
            let element = document.getElementById("body"); 
            element.classList.add("active-z");
            break;
    }
    button.classList.add("active");
}))

function createNameValueList(name, value){
    temporalList.push([name, value])
}

function flushNameValueList (id, property){
    tabList[id][property] = temporalList;
    temporalList = [];
}

let navElement;

function deleteTabFromList(id){
    if(tabList[id]){
        javaFXController.info(id);
        navElement = document.querySelector(`div[data-id="${id}"]`);
        deleteTab(navElement);
    }
}

function saveAndExit(){
    try{
        saveChanges();
        for(let element in tabList){
            javaFXController.saveTemporal(element, tabList[element]);
        }
    }catch (ex){
        javaFXController.debug(ex);
    }
}

function createQueryString(parameterValuePairs) {
    // Initialize an empty array to store the key-value pairs as strings
    const queryParams = [];

    // Iterate through the parameter-value pairs and format them as strings
    for (const [param, value] of parameterValuePairs) {
        // Convert the parameter and value to strings and encode them for URLs
        if(value === "")continue;
        const paramStr = encodeURIComponent(param);
        const valueStr = encodeURIComponent(value);

        // Append the formatted key-value pair to the array
        queryParams.push(`${paramStr}=${valueStr}`);
    }

    // Join the key-value pairs with '&' to create the final query string
    const queryString = queryParams.join('&');

    return queryString;
}


