[![Codacy Badge](https://api.codacy.com/project/badge/Grade/9b76ad18252a4b9c80b40f4115e98a76)](https://www.codacy.com/app/biosphere.dev/promcord?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Biospheere/promcord&amp;utm_campaign=Badge_Grade)
[![Build Status](https://travis-ci.com/Biospheere/promcord.svg?branch=master)](https://travis-ci.com/Biospheere/promcord)
[![GitHub contributors](https://img.shields.io/github/contributors/biospheere/promcord.svg)](https://github.com/Biospheere/promcord/graphs/contributors/)
[![c0debase Discord](https://discordapp.com/api/guilds/361448651748540426/embed.png)](discord.gg/cDV38ht)

# promcord

Promcord is a Discord bot which provides metrics from a Discord server to create insight and alerting on actions and messages. Using [Grafana](https://grafana.com/) you can easily visualize all your metrics in beautiful dashboards.

## Installation

Use Docker Compose to install promcord. 
If you have already created a Discord Bot you can skip this step. 
Otherwise, see [this](https://github.com/reactiflux/discord-irc/wiki/Creating-a-discord-bot-&-getting-a-token) introduction on how to create a Discord bot and get the token of your new bot.

Create a .env file and set the required values in it.

```
DISCORD_TOKEN=<your token>
```

Start the Docker containers with the following command: 

```
docker-compose up -d
```

To run Grafana open your browser and go to [http://your-ip:80](http://localhost:80). Then follow [this](https://grafana.com/docs/guides/getting_started/) guide to configure your Grafana server. 



## Screenshots

![Image of Grafana](https://i.imgur.com/2EArDun.png)

## Built with 

- [JDA](https://github.com/DV8FromTheWorld/JDA) - Java wrapper for the popular chat & VOIP service  [@discordapp](https://github.com/discordapp)
- [Prometheus](https://prometheus.io/) 
- [Perspective API](https://www.perspectiveapi.com/) 

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.
Easiest way of reaching me is via [Discord](https://c0debase.de/).

## [License](https://github.com/Biospheere/promcord/blob/master/LICENSE)
MIT © [Niklas](https://github.com/Biospheere/)
