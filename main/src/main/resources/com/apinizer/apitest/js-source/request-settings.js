const requestBtn = document.getElementById("request-btn");
const requestContainer = document.getElementById("request-container")

requestBtn.addEventListener("click", () => {
    requestContainer.classList.toggle("active-flex");
})

window.addEventListener("click" , (e) => {
    if(e.target != requestBtn && !requestContainer.contains(e.target) && !requestContainer.contains(e.target)){
        requestContainer.classList.remove("active-flex");
    }
})

const requests = document.querySelectorAll("#request-container > div");



requests.forEach(item => {
    item.addEventListener("click", () => {
    requestBtn.dataset.value = item.dataset.value; 
    requestBtn.children[0].textContent = item.textContent;
    requestContainer.classList.remove("active-flex")
})});

const endpointBtn = document.getElementById("endpoint-btn");
const endpointContainer = document.getElementById("endpoint-container")

endpointBtn.addEventListener("click", () => {
    endpointContainer.classList.toggle("active-flex");
})

window.addEventListener("click" , (e) => {
    if(e.target != endpointBtn && !endpointContainer.contains(e.target) && !endpointContainer.contains(e.target)){
        endpointContainer.classList.remove("active-flex");
    }
})

const endpoints = document.querySelectorAll("#endpoint-container > div");



endpoints.forEach(item => {
    item.addEventListener("click", () => {
    endpointBtn.dataset.value = item.dataset.value; 
    endpointBtn.children[0].textContent = item.textContent;
    endpointContainer.classList.remove("active-flex")
})});