const headerValues = [
    "Accept",
    "Accept-Charset",
    "Accept-Datetime",
    "Accept-Encoding",
    "Accept-Language",
    "Authorization",
    "Cache-Control",
    "Connection",
    "Content-Length",
    "Content-MD5",
    "Content-Type",
    "Cookie",
    "Date",
    "Expect",
    "Forwarded",
    "From",
    "Host",
    "If-Match",
    "If-Modified-Since",
    "If-None-Match",
    "If-Range",
    "If-Unmodified-Since",
    "Max-Forwards",
    "Origin",
    "Pragma",
    "Proxy-Authorization",
    "Range",
    "Referer [sic]",
    "TE",
    "Upgrade",
    "User-Agent",
    "Via",
    "Warning"
];

const mimeTypeValues = [
    "application/msword",
    "application/mspowerpoint",
    "application/mime",
    "application/json",
    "application/javascript",
    "video/quicktime",
    "application/java",
    "video/msvideo",
    "video/mpeg",
    "video/avi",
    "text/xml",
    "text/plain",
    "text/javascript",
    "text/html",
    "text/ecmascript",
    "text/css",
    "multipart/x-zip",
    "application/excel",
    "multipart/x-gzip",
    "image/tiff",
    "image/png",
    "image/jpeg",
    "image/gif",
    "image/bmp",
    "audio/wav",
    "audio/mpeg3",
    "audio/mpeg",
    "audio/midi",
    "application/ecmascript",
    "audio/basic",
    "application/zip",
    "application/xml",
    "application/x-zip-compressed",
    "application/rtf",
    "application/powerpoint",
    "application/postscript",
    "application/plain",
    "application/pdf",
    "application/octet-stream",
    "application/base64"
];

const headerRoot = document.getElementById("headers-input-container");
const headerAddBtn = document.getElementById("headers-add-btn");


headerAddBtn.addEventListener("click", () => {
   addNewHeader("", "")
})

function addNewHeader(value1, value2){
    let newDiv = document.createElement("div");
    newDiv.classList.add("input");
    newDiv.classList.add("header-input-element-container");

    let input1 = createHeader("Search", headerValues);
    let input2 = createHeader("Search", mimeTypeValues);


    let removeBtn = document.createElement("button");
    removeBtn.innerHTML = "<i class='fa-solid fa-square-minus'></i>";
    removeBtn.classList.add("remove-btn");

    removeBtn.addEventListener("click", () => {
        headerRoot.removeChild(newDiv);
    })
    input1.firstElementChild.value = value1;
    input2.firstElementChild.value = value2;

    newDiv.appendChild(input1);
    newDiv.appendChild(input2);
    newDiv.appendChild(removeBtn);
    headerRoot.appendChild(newDiv);
}


function createHeader(placeholder, array) {
    var filteredValues = [];
    let time;
    var newDiv = document.createElement("div");
    var input = document.createElement("input");
    var container = document.createElement("div");
    newDiv.classList.add("header-container");
    container.classList.add("search-container");

    window.addEventListener("click" , (e) => {
        if(e.target !== input && !container.contains(e.target)){
            container.classList.remove("active-flex");
        }
    })

    input.type = "text";
    input.style.width = "100%";
    input.placeholder = placeholder;
    input.classList.add("text-field");
    newDiv.appendChild(input);
    newDiv.appendChild(container);
    input.addEventListener("input", (e) => {
        if(time){
            clearTimeout(time);
        }
        time = setTimeout(() => {
            while (container.firstChild) {
                container.removeChild(container.firstChild);
            }
            filteredValues = array.filter((item) => item.toLowerCase().startsWith(e.target.value.toLowerCase()));
            if(filteredValues.length != 0 && e.target.value){
                let component;
                filteredValues.forEach(item => {
                    component = document.createElement("button");
                    component.classList.add("headers-search-component");
                    component.textContent = item;
                    container.appendChild(component);

                    component.addEventListener("click", () => {
                        e.target.value = item;
                        container.classList.remove("active-flex");
                    })
                }) 
                container.classList.add("active-flex");
            }else{
                container.classList.remove("active-flex");
            }
        }, 500)
    })

    return newDiv;
}
