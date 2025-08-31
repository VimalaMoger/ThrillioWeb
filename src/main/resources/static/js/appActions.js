// Adding  new field in html form
document.addEventListener('DOMContentLoaded', function(event) {
     setTimeout(() => {

            event.preventDefault();
            const inputContainer_author = document.getElementById('input-container-a');
            const addFieldButton_author = document.getElementById('addFieldButton-a');

            const inputContainer_publisher = document.getElementById('input-container-b');
            const addFieldButton_publisher = document.getElementById('addFieldButton-b');
            let fieldCounter_a = 0; // To ensure unique names/ids
            let fieldCounter_p = 0;

            addFieldButton_author.addEventListener('click', function(event) {
                event.preventDefault();
                setTimeout(() => {
                    const newInput = document.createElement('input');
                    newInput.type = 'text';
                    newInput.name = 'authors[' + fieldCounter_a + '].name';   // List binding
                    newInput.pattern = '[a-z0-9]{1, 12}';
                    inputContainer_author.appendChild(newInput);
                    fieldCounter_a++;
               }, 500);
            });

             addFieldButton_publisher.addEventListener('click', function(event) {
                event.preventDefault();
                setTimeout(() => {
                    const newInput = document.createElement('input');
                    newInput.type = 'text';
                    newInput.name = 'publishers[' + fieldCounter_p + '].name'; //List binding
                    newInput.pattern = '[a-z0-9]{1, 12}';
                    inputContainer_publisher.appendChild(newInput);
                    fieldCounter_p++;
               }, 500);
          });
     }, 500);
});



