var imageButton = document.getElementById("button");
var nameButton = document.getElementById("buttonName");
var url = "https://arep-walteros-lab4.herokuapp.com/Nsapps/hello?value=";

imageButton.addEventListener('click', function () {
    $('#image').toggle('slow');
});

nameButton.addEventListener('click', function () {
    $("#hello").empty();
    var name = document.getElementById("name").value;
    if(name===""){
        alert("Please insert a name");
    } else {
        axios.get(url+name)
            .then(res => {
                $("#hello").append(res.data);
            }).catch(err => {
                console.log(err);
        });
    }
});