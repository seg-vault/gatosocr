$.ajax({
  url: "/files",
  success: function( result ) {
    $( "#pending-files" ).html(result);
  }
});