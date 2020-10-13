function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for(var i = 0; i <ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

function addTransaction() {
    console.log('add transaction');
    $.post('http://localhost:' + apiPort + '/transactions/new?token=' + encodeURIComponent(getCookie('accessToken')),
        {
            recipient: $('#recipient').val(),
            description: $('#description').val(),
            amount: $('#amount').val()
        },
        function(data) {
            if(data) {
                window.location.replace("http://localhost:" + ownPort + "/");
            }
            else {
                $('#error').text('Unable to add transaction!');
            }
        }
    );
}

function listTransactions() {
    $.getJSON('http://localhost:' + apiPort + '/transactions?token=' + encodeURIComponent(getCookie('accessToken')),
        function (data) {
            console.log(data);
            var container = $('#resultContainer');
            container.children().remove();

            var balance = $('<div class="balance"></div>');
            balance.text('Balance: £' + data.accountBalance);
            balance.appendTo(container);

            $.each(data.transactions, function (index, value) {
                var t = $('<div class="transaction"></div>');

                var desc = $('<div class="description"></div>');
                desc.html(value.description);
                desc.appendTo(t);

                var amount = $('<div class="amount"></div>');
                amount.text('£' + value.amount);
                amount.appendTo(t);

                if(value.amount >= 0.0) {
                    t.addClass('credit');
                }
                else {
                    t.addClass('debit');
                }

                t.appendTo(container);
            });
        });
}

$(document).ready(function () {
    $('#listTransactions').click(listTransactions);
    $('#addTransaction').click(addTransaction);
});
