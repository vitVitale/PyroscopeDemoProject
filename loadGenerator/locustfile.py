from locust import HttpUser, task, between


class UserApiTest(HttpUser):
    # wait_time = between(1, 3)
    user_id = None

    @task
    def create_user(self):
        with self.client.post(
                "/api/users",
                json={"name": "John Doe", "email": "john@example.com"},
                catch_response=True
        ) as response:
            if response.status_code == 201:
                # Save the created user's ID for later requests
                self.user_id = response.json().get("id")
                response.success()
            else:
                response.failure(f"Failed to create user: {response.text}")

    @task
    def get_all_users(self):
        self.client.get("/api/users")

    @task
    def get_user_by_id(self):
        if self.user_id:
            self.client.get(f"/api/users/{self.user_id}")
