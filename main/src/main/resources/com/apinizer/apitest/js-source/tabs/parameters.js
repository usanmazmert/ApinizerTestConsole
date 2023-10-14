const root = document.getElementById("parameters-input-container");
const addBtn = document.getElementById("parameters-add-btn");


addBtn.addEventListener("click", () => {
    addNewParam("", "")
})

function addNewParam(value1, value2){
    let disabled = true;
    let newDiv = document.createElement("div");
    newDiv.classList.add("input")
    let input1 = createParameter("");
    let input2 = createParameter("", "white");
    input1.addEventListener("input", (e) => {
        if(e.target.value !== ""){
            input2.disabled = false;
            input2.style.backgroundColor = "white";
        }else{
            input2.disabled = true;
            input2.style.backgroundColor = "rgb(200, 200, 200)";
            input2.value = "";
        }
    })

    input2.addEventListener("input", (e) => {
        try{
            let child;
            tabList[activeElementId].parameters = [];
            for(let i = 0; i < root.children.length; i++) {
                child = root.children[i];
                tabList[activeElementId].parameters.push([child.children[0].value, child.children[1].value]);
            }
            let str = urlInput.value;
            let index = str.indexOf("?");
            if(index !== -1){
                str = str.substring(0, index);
            }
            str += "?";
            str += createQueryString(tabList[activeElementId].parameters);
            urlInput.value = str;
        }catch (ex){
            javaFXController.debug(ex);
        }
    })
    let removeBtn = document.createElement("button");
    removeBtn.innerHTML = "<img style=\"object-fit: cover; max-width: 0.75rem;margin-bottom: -1px;\" src=\"../images/326681_circle_remove_icon.png\" alt=\"remove-btn\">";
    removeBtn.classList.add("remove-btn");
    removeBtn.addEventListener("click", () => {
        root.removeChild(newDiv);

    })
    input1.value = value1;
    input2.value = value2;
    if(!value1){
        input2.disabled = true;
        input2.style.backgroundColor = "rgb(200, 200, 200)";
    }
    newDiv.appendChild(input1);
    newDiv.appendChild(input2);
    newDiv.appendChild(removeBtn);
    root.appendChild(newDiv);
}

function createParameter(placeholder, color) {
    const input = document.createElement("input");
    input.type = "text";
    input.style.width = "100%";
    input.placeholder = placeholder;
    input.style.backgroundColor = color;
    input.classList.add("text-field");
    return input;
}