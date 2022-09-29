getTempFiles();
getPermFiles();
//setInterval(getTempFiles,30000);
//setInterval(getPermFiles,30000);

function getTempFiles(){
    $.ajax({
        url: "/files/tmp",
        success: function( result ) {
          $( "#pending-files" ).html(result);
        }
    });
}
function getPermFiles(){
    $.ajax({
        url: "/files/perm",
        success: function( result ) {
          $( "#completed-files" ).html(result);
        }
    });
}