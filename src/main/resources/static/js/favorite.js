$('.like_btn').click(function() {
	var req = new XMLHttpRequest();
	var url = "/like/";
	req.open('POST',url);
	req.setRequestHeader('content-type', 'application/x-www-form-urlencoded;charset=UTF-8');
	req.send('id=' + $(this).attr('tweetId'));
	req.onreadystatechange = function() {
		if (req.readyState === 4 && req.status === 200) {
			location.reload();
		}
	}
});

$('.unlike_btn').click(function() {
	var req = new XMLHttpRequest();
	var url = "/unlike/";
	req.open('POST',url);
	req.setRequestHeader('content-type', 'application/x-www-form-urlencoded;charset=UTF-8');
	req.send('id=' + $(this).attr('tweetId'));
	req.onreadystatechange = function() {
		if (req.readyState === 4 && req.status === 200) {
			location.reload();
		}
	}
});
