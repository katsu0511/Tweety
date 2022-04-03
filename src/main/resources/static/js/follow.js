$('#follow').click(function() {
	var req = new XMLHttpRequest();
	var url = "/follow";
	req.open('POST',url);
	req.setRequestHeader('content-type', 'application/x-www-form-urlencoded;charset=UTF-8');
	req.send('id=' + $(this).attr('followedId'));
	req.onreadystatechange = function() {
		if (req.readyState === 4 && req.status === 200) {
			location.reload();
		}
	}
});

$('#unfollow').click(function() {
	var unfollow = $('#unfollow_mes').val();
	if(confirm(unfollow)){
		var req = new XMLHttpRequest();
		var url = "/unfollow";
		req.open('POST',url);
		req.setRequestHeader('content-type', 'application/x-www-form-urlencoded;charset=UTF-8');
		req.send('id=' + $(this).attr('followedId'));
		req.onreadystatechange = function() {
			if (req.readyState === 4 && req.status === 200) {
				location.reload();
			}
		}
	}
});
