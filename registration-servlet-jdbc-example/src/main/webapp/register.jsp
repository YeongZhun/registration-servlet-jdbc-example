<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>Registration Form</title>
</head>

<body>
    <div align="center">
        <h1>Registration Form</h1>
        <form action="eregister" method="post">
            <table style="width: 100%">
                <tr>
                    <td>Name</td>
                    <td><input type="text" name="name" required /></td>
                </tr>
                <tr>
                    <td>Phone Number</td>
                    <td><input type="text" name="phone_number" required /></td>
                </tr>
                <tr>
                    <td>Address</td>
                    <td><input type="text" name="address" required /></td>
                </tr>
            </table>
            <input type="submit" value="Submit" />
        </form>
    </div>
</body>

</html>
