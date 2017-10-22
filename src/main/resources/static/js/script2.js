jQuery(document).ready(function($) {
	$("#kota").change(function(event) {
		event.preventDefault();
		changeKecamatan($("#kota option:selected").val());
	});
	$("#kecamatan").change(function(event) {
		event.preventDefault();
		changeKelurahan($("#kecamatan option:selected").val());
	});
});

function changeKecamatan(kota){
	$('#kecamatan').empty();
	var message = "kota="+kota;
	$.ajax({
		type : "GET",
		contentType : "text/plain",
		url : "/api/getKecamatan",
		data : message,
		dataType : 'json',
		timeout : 100000,
		success : function(data) {
			$.each(data, function(i, row) {
				$('<option>').attr('value', row.kode).text(row.nama).appendTo('#kecamatan');
			});
			$("#kecamatan").val($("#kecamatan option:first").val());
			changeKelurahan($("#kecamatan option:selected").val());
		},
		error : function(e) {
			console.log("ERROR: ", e);
		},
		done : function(e) {
			console.log("DONE");
		}
	});
}

function changeKelurahan(kecamatan){
	$('#kelurahan').empty();
	var message = "kecamatan="+kecamatan;
	$.ajax({
		type : "GET",
		contentType : "text/plain",
		url : "/api/getKelurahan",
		data : message,
		dataType : 'json',
		timeout : 100000,
		success : function(data) {
			$.each(data, function(i, row) {
				$('<option>').attr('value', row.kode).text(row.nama).appendTo('#kelurahan');
			});
		},
		error : function(e) {
			console.log("ERROR: ", e);
		},
		done : function(e) {
			console.log("DONE");
		}
	});
	
}