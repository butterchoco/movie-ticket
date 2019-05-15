
        var createCallback = function(i) {
            var count = $("#ticket-number").val();
            $(".pressed").removeClass("pressed");
            for (let cnt = 0; cnt < count; cnt++) {
                $("#sqr-"+(i+cnt)).addClass("pressed");
            }
            $('.footer').show();
        }

        function ajaxSeat() {
            var theatre = $(".cinema").attr("id");
            var theatreId = theatre.split("-")[1];
            $.ajax({
                method: "GET",
                url: '/seat',
                success: function(data) {
                    var seat = data[theatreId-1].rows;
                    var rowDivision = Math.round((seat.length - (seat.length*2/3))/2);
                    var count = 0;
                    var middle = "";
                    var far = "";
                    for (var i = 0; i < seat.length; i++) {
                        if (seat[i].type == "Middle") {
                            if (count == 0) {
                                middle += "<div id='rowseat'>";
                            }
                            middle += "<button onclick='createCallback("+(i+1)+")' id='sqr-" + (i+1) + "' class='square white premium'>" + (i+1) + "</button>";
                            count += 1;
                            if (count == rowDivision || seat[i+1].type === "Far") {
                                middle += "</div>";
                                count = 0;
                            }
                        } else {
                            if (count == 0) {
                                far += "<div id='rowseat'>";
                            }
                            far += "<button onclick='createCallback("+(i+1)+")' id='sqr-" + (i+1) + "' class='square white vvip'>" + (i+1) + "</button>";
                            count += 1;
                            if (count == rowDivision) {
                                far += "</div>";
                                count = 0;
                            }
                        }
                    }
                    $("#middle").append(middle);
                    $("#far").append(far);
                }
            });
        }

        function ajaxMovie(id) {
            $(".time div button#"+id).prop("disabled", true);
            var theatreId = $(".cinema").attr("id").split("-")[1];

            $.ajax({
                method: "GET",
                url: "/bookings-saved",
                success: function(data) {
                    for (let i = 0; i < data.length; i++) {
                        if (theatreId == data[i].movieSession.theatre.id && id == data[i].movieSession.id) {
                            $("#sqr-"+data[i].seat.seatNumber).prop("disabled", true);
                        } else {
                            $("#sqr-"+data[i].seat.seatNumber).prop("disabled", false);
                        }
                    }
                }
            })
        }

        function ajaxVerify(sessionId) {
        $.ajax({
                                                       method: "POST",
                                                       url: "http://c8-tickets-api.herokuapp.com/bookings/"+sessionId+"/verify",
                                                       data: {
                                                            "email":"ahmadsupri1999@gmail.com",
                                                            "apiKey":"ceritanyabiaraman"
                                                       },
                                                       success: function(data) {
                                                            alert("SUCCESS")
                                                            window.location.replace("/");
                                                       }
                                                })
        }

        $(document).ready(function () {
            ajaxSeat();

            $(window).load(function () {
                $('.footer').hide();
                $('.time div button').click(function() {
                    $('.time div button').prop("disabled", false);
                    ajaxMovie($(this).attr("id"));
                });

                const sessionId = $(".time").attr("id").split("-")[1];
                ajaxMovie(sessionId);

                $(".accept").click(function() {
                    var price = ""
                    if ($(".pressed").hasClass('premium')) {
                         price = $("#premium").text();
                    } else {
                         price = $("#vvip").text();
                    }
                    console.log(price)
                    var time = $(".time div button:disabled").attr("id");
                    var seat = document.getElementsByClassName("pressed");
                    var seatIds = "";
                    for (let i = 0; i < seat.length; i++) {
                        seatIds += seat[i].getAttribute("id").split("-")[1];
                        if (i < seat.length-1) {
                            seatIds += ",";
                        }
                    }
                    $.ajax({
                           method: "POST",
                           url: "http://c8-tickets-api.herokuapp.com/bookings",
                           data: {
                                "sessionId":time,
                                "seatIds":seatIds,
                                "price": price
                           },
                           success: function(data) {
                                ajaxVerify(time)
                           }
                    })

                })
            });
        });