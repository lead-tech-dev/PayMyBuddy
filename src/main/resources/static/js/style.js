$(document).ready(function () {
    jQuery('<div class="amount-nav"><button class="amount-button amount-up">&#xf106;</button><button class="amount-button amount-down">&#xf107</button></div>').insertAfter('.amount input');
    jQuery('.amount').each(function () {
        var spinner = jQuery(this),
            input = spinner.find('input[type="number"]'),
            btnUp = spinner.find('.amount-up'),
            btnDown = spinner.find('.amount-down'),
            min = input.attr('min'),
            max = input.attr('max');

        btnUp.click(function () {
            var oldValue = parseFloat(input.val());
            if (oldValue >= max) {
                var newVal = oldValue;
            } else {
                var newVal = oldValue + 1;
            }
            spinner.find("input").val(newVal);
            spinner.find("input").trigger("change");
        });

        btnDown.click(function () {
            var oldValue = parseFloat(input.val());
            if (oldValue <= min) {
                var newVal = oldValue;
            } else {
                var newVal = oldValue - 1;
            }
            spinner.find("input").val(newVal);
            spinner.find("input").trigger("change");
        });

    });

    function mobileMenu() {
        let element = document.getElementsByClassName('header-area__mobile');
        element[0].classList.toggle('open');
        console.log(element[0].classList)

    }

    document.getElementById('mainmenu').addEventListener('click', mobileMenu);
});