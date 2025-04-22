# Functional Design

## Login Functionality

- **Email-Password Login**:
  - Verify the email and password.
  - Check if the user is locked.
  - Increment the number of failed attempts on each failed login.
  - Lock the user if the number of failed attempts exceeds the limit (e.g., 5).

## Password Management

- **Change Password**: Allow logged-in users to update their password.
- **Reset Password**: Generate a unique `reset_token`, store it in the `password_resets` table, and set an expiration time.
- **Forgot Password**: Send a reset link to the user's email. The user can use the link to verify the `reset_token` and set a new password.