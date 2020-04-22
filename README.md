[![Codacy Badge](https://api.codacy.com/project/badge/Grade/9b76ad18252a4b9c80b40f4115e98a76)](https://www.codacy.com/app/biosphere.dev/promcord?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Biospheere/promcord&amp;utm_campaign=Badge_Grade)
[![Build Status](https://github.com/biospheere/promcord/workflows/Build/badge.svg?branch=master)](https://github.com/biospheere/promcord/actions)
[![Dependabot](https://api.dependabot.com/badges/status?host=github&repo=Biospheere/c0debaseBot)](https://dependabot.com/)
[![GitHub contributors](https://img.shields.io/github/contributors/biospheere/promcord.svg)](https://github.com/Biospheere/promcord/graphs/contributors/)

# promcord

## What is promcord?

Promcord is a Discord bot which provides metrics from a Discord server to create insight and alerting on actions and messages. Using [Grafana](https://grafana.com/) you can easily visualize all your metrics in beautiful dashboards.

## Table of Contents

  - [Contributing](#contributing)
  - [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Docker Installation ](#docker-installation)
    - [Developer Installation ](#developer-installation)
  - [Collected data](#collected-data)
  - [Screenshots](#screenshots)
  - [Built with](#built-with)
  - [License](#license)


## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.
Easiest way of reaching me is via [Discord](https://c0debase.de/).

## Getting Started

This section provides a high-level requirement & quick start guide. **For detailed installations, such as getting started with Maven, Docker, or Grafana, please check out their docs.**


### Prerequisites

- [JDK](https://www.oracle.com/technetwork/java/javase/downloads/index.html) 
- [Maven](https://maven.apache.org/)
- [Prometheus](https://prometheus.io/)
- [Grafana](https://grafana.com/)

#### Docker Installation 

1. Follow the [Docker CE install guide](https://docs.docker.com/install/) and the [Docker Compose install guide](https://docs.docker.com/compose/install/), which illustrates multiple installation options for each OS.
1. Set up your environment variables/secrets in `.env` file
```
DISCORD_TOKEN=<your token>
```
1. Download the [prometheus.yml](prometheus.yml) file to your current directory.
1. Run the Docker App with `docker-compose up -d`
1. That's it! Go to [http://your-ip:80](http://localhost:80) to access Grafana. Then follow [this](https://grafana.com/docs/guides/getting_started/) guide to configure your Grafana server. 

### Developer Installation

1. Make sure all the prerequisites are installed.
1. Fork promcord repository, ie. https://github.com/Biospheere/promcord/fork
1. Clone your forked repository, ie. `git clone https://github.com/<your-username>/promcord.git`
1. Set up your environment variables/secrets
```
DISCORD_TOKEN=<your token>
```
1. Use [prometheus.yml](prometheus.yml) for Prometheus (Edit target hostname!)
1. That's it! Go to [http://your-ip:grafana-port](http://localhost:3000) to access Grafana. Then follow [this](https://grafana.com/docs/guides/getting_started/) guide to configure your Grafana server. 

## Collected data

- Channel ID of voice and text channels
- Word count and length from a message
- Guild ID 
- Emote Name
- Game name provided by a Discord RPC
- ToxicityScore calculated by PerspectiveAPI
- Amount of members on a guild
- Amount of online members on a guild
- Time spent streaming in a voice channel
- Time spent in a voice channel
- Nitro Boosts
- Discord Gateway Ping
- Discord Rest Ping
  
## Screenshots

![Image of Grafana](https://i.imgur.com/2EArDun.png)

## Built with 

- [JDA](https://github.com/DV8FromTheWorld/JDA) - Java wrapper for the popular chat & VOIP service  [@discordapp](https://github.com/discordapp)
- [Prometheus](https://prometheus.io/) 
- [Perspective API](https://www.perspectiveapi.com/) 

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details

<p align="center">
  <img alt="Haha yes " width="250px" src="https://i.imgur.com/5bXJeZt.png">
  <br>
</p>

