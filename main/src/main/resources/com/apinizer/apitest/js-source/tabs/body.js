const bodyRawButton = document.getElementById("radio-raw");

const bodyUrlButton = document.getElementById("radio-url-encoded");

const requestBody = document.getElementById("request-body");

const urlEncoded = document.getElementById("request-url-encoded");

let requestBodyType = "raw";

bodyRawButton.addEventListener("click", () => {
    requestBody.classList.add("active-z");
    urlEncoded.classList.remove("active-block");
    response.classList.remove("url-encoded-active");
    requestBodyType = "raw";
})

bodyUrlButton.addEventListener("click", () => {
    urlEncoded.classList.add("active-block");
    response.classList.add("url-encoded-active");
    requestBody.classList.remove("active-z");
    requestBodyType = "url-encoded";
})

const bodyAddBtn = document.getElementById("encoded-add-btn");

const encodedRoot = document.getElementById("encoded-input-container");


bodyAddBtn.addEventListener("click", () => {
    addUrlEncodedBody("", "", "");
})

function addUrlEncodedBody(value1, value2, value3){

    let newDiv = document.createElement("div");
    newDiv.classList.add("input")
    newDiv.classList.add("urlencoded-input-element-container");

    let input1 = createParameter("");
    let input2 = createParameter("","white");
    let input3 = createParameter("", "white");
    //Temp Area
    input3.disabled = true;
    //Temp End
    let removeBtn = document.createElement("button");
    removeBtn.innerHTML = "<img style=\"object-fit: cover; max-width: 0.75rem;margin-bottom: -1px;\" src=\"../images/326681_circle_remove_icon.png\" alt=\"remove-btn\">";
    removeBtn.classList.add("remove-btn");

    removeBtn.addEventListener("click", () => {
        encodedRoot.removeChild(newDiv);
    })

    input1.value = value1;
    input2.value = value2;
    input3.value = value3;

    newDiv.appendChild(input1);
    newDiv.appendChild(input2);
    newDiv.appendChild(input3);
    newDiv.appendChild(removeBtn);
    encodedRoot.appendChild(newDiv);

}