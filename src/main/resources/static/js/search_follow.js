const search_input = $('#search_input');
const search_result = $('.search_result');
const searchResult = search_result.toArray();

search_input.keyup(function() {
	if (search_input.val() == '') {
		search_result.css('display','block');
	} else {
		search_result.css('display','none');
		$.each(searchResult,function() {
			var user_name = $(this).find('.user_name').text();
			var userName = $.trim(user_name);
			var prof_name = $(this).find('.prof_name').text();
			var profName = $.trim(prof_name);
			if(userName.indexOf(search_input.val()) > -1 || profName.indexOf(search_input.val()) > -1) {
				$(this).css('display','block');
			}
		});
	}
});
