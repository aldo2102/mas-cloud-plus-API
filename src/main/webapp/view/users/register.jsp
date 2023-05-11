<script>
    function validate()
    {
        var name = document.form.fullname.value;
        var email = document.form.email.value;
        var username = document.form.username.value;
        var password = document.form.password.value;
        var conpassword = document.form.conpassword.value;

        if (fullname == null || fullname == "")
        {
            alert("Full Name can't be blank");
            return false;
        } else if (email == null || email == "")
        {
            alert("Email can't be blank");
            return false;
        } else if (username == null || username == "")
        {
            alert("Username can't be blank");
            return false;
        } else if (password.length < 6)
        {
            alert("Password must be at least 6 characters long.");
            return false;
        } else if (password != conpassword)
        {
            alert("Confirm Password should match with the Password");
            return false;
        }
    }
</script> 

<!-- Page Top section -->
<section class="page-top-section set-bg" data-setbg="img/page-top-bg/3.jpg">
    <div class="container">
        <h2>Register</h2>
    </div>
</section>
<!-- Page Top section end -->


<!-- Services section -->
<section class="contact-section spad">
    <div class="container">
        <div class="row centered-form">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Please sign up</h3>
                    <p></p>
                </div>
                <div class="panel-body">
                    <form role="form"  name="form" action="UserServelet" method="post" onsubmit="return validate()">
                        <div class="row">
                            <div class="col-xs-6 col-sm-6 col-md-6">
                                <div class="form-group">
                                    <input type="text" name="name" id="name" class="form-control input-sm" placeholder="Full Name">
                                </div>
                            </div>
                            <div class="col-xs-6 col-sm-6 col-md-6">
                                <div class="form-group">
                                    <input type="text" name="user" id="user" class="form-control input-sm" placeholder="User Name">
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <input type="email" name="email" id="email" class="form-control input-sm" placeholder="Email Address">
                        </div>

                        <div class="row">
                            <div class="col-xs-6 col-sm-6 col-md-6">
                                <div class="form-group">
                                    <input type="password" name="password" id="password" class="form-control input-sm" placeholder="Password">
                                </div>
                            </div>
                            <div class="col-xs-6 col-sm-6 col-md-6">
                                <div class="form-group">
                                    <input type="password" name="conpassword" id="conpassword" class="form-control input-sm" placeholder="Confirm Password">
                                </div>
                            </div>
                        </div>

                        <input type="submit" value="Register" class="btn btn-info btn-block">

                    </form>
                </div>
            </div>
        </div>
    </div>
</section>
<!-- Services section end -->
