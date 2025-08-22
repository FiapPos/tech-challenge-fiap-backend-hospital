import os
import threading

services = [
    "orchestrator-service",
    "notificacao-service",
    "historico-service",
    "agendamento-service",
    "usuario-service"
]

threads = []

def build_app(app):
    threads.append(app)
    print(f"Building {app}...")
    os.system(f"cd {app} && mvn clean package -DskipTests")
    print(f"{app} build finished!")
    threads.remove(app)

def docker_up():
    print("Starting containers...")
    os.system("docker compose up --build -d")
    print("Containers are up!")

def remove_containers():
    print("Stopping existing containers...")
    os.system("docker compose down")

if __name__ == "__main__":
    remove_containers()

    for app in services:
        threading.Thread(target=build_app, args=(app,)).start()

    while threads:
        pass

    docker_up()
