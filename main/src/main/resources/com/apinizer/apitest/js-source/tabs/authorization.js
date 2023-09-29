const inputField = document.getElementById("authorization-input-field");

const typeSelector = document.getElementById("auth-btn");

const authContainer = document.getElementById("auth-container")

typeSelector.addEventListener("click" , () => {
    authContainer.classList.toggle("active-flex")
})

window.addEventListener("click" , (e) => {
    if(e.target != typeSelector && !authContainer.contains(e.target) && !typeSelector.contains(e.target)){
        authContainer.classList.remove("active-flex");
    }
})

const authTypes = document.querySelectorAll("#auth-container > div");

authTypes.forEach(item => {
    item.addEventListener("click", () => {
        changeAuthEvent(item.textContent);
})});

function changeAuthEvent(authType){
    typeSelector.children[0].textContent = authType;
    let newDiv = document.createElement("div");
    switch(authType.trim()){
        case "No Auth":
            inputField.innerHTML = "";
            break;
        case "API Key":
        {
            inputField.innerHTML = "";
            let components = createAuthorization("Key");
            newDiv.append(components);
            components = createAuthorization("Value");
            newDiv.append(components);
            break;
        }
        case "Bearer Token":
        {
            inputField.innerHTML = "";
            let components = createAuthorization("Token");
            newDiv.append(components);
            break;
        }
        case "Basic Auth":
        {
            inputField.innerHTML = "";
            let components = createAuthorization("Username");
            newDiv.append(components);
            components = createAuthorization("Password");
            newDiv.append(components);
            break;
        }
        default:
            inputField.innerHTML = "";
    }
    newDiv.classList.add("text-component")
    inputField.append(newDiv);
    authContainer.classList.remove("active-flex");

}

function createAuthorization(name){
    let container = document.createElement("div");
    let input = document.createElement("input");
    input.classList.add("text-field");
    input.placeholder = name;
    let label = document.createElement("label");
    label.classList.add("text-label");
    label.textContent = name;
    container.append(label);
    container.append(input);
    container.classList.add("text-container");
    return container;
}