
const responseBodyBtn = document.getElementById("response-body-btn");
const responseHeadersBtn = document.getElementById("response-headers-btn");

const responseBody = document.getElementById("response-body");
const responseHeaders = document.getElementById("response-headers");

responseBodyBtn.addEventListener("click", () => {
    responseHeadersBtn.classList.remove("active"); 
    responseBodyBtn.classList.add("active"); 
    responseBody.classList.add("active-z");
    responseHeaders.classList.remove("active-block");
})

responseHeadersBtn.addEventListener("click", () => {
    responseBodyBtn.classList.remove("active"); 
    responseHeadersBtn.classList.add("active"); 
    responseHeaders.classList.add("active-block");
    responseBody.classList.remove("active-z");
})


const responseHeaderContainer = document.getElementById("response-input-container");

function createHeaderContainer(key, value){
    let newDiv = document.createElement("div");
    newDiv.classList.add("input")

    let div1 = createResponseHeader(key);
    let div2 = createResponseHeader(value);

    newDiv.appendChild(div1);
    newDiv.appendChild(div2);
    responseHeaderContainer.appendChild(newDiv);
}
function createResponseHeader(text) {
    var div = document.createElement("div");
    div.textContent = text;
    div.classList.add("header-field");
    return div;
}
