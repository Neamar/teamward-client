# Teamward: League Of Legends game statistics
> This is the client component. The server is [here](https://github.com/neamar/teamward-server).

![Don't be that guy.](banner_text.jpg)

Get information about your current games:

* Champions you're facing
* Champions mastery score
* Summoner rank
* Is it his main champ?
* Premade information
* Matchup statistics
* And more!

TeamWard isn't endorsed by Riot Games and doesn't reflect the views or opinions of Riot Games or anyone officially involved in producing or managing League of Legends. League of Legends and Riot Games are trademarks or registered trademarks of Riot Games, Inc. League of Legends © Riot Games, Inc.


For more information about TeamWard, please visit https://github.com/neamar/teamward-server


## Client architecture
Most of the code lives in the GameActivity, through different fragments.

The least intutitive thing is probably how to add a new kind of tip, since it requires change in a lot of files.

### How to make a new tip?
* Create a new class in `tip`, extending Tip, or reuse an existing one (for instance PlayerStandardTip). It should be a very simple holder to store data you'll need for display, nothing more.
* Create a new `tip.builder` extending TipBuilder
    - Implement the `getTips()` method, returning an ArrayList<? extends Tip>
* Create a new layout, extending CardView.
* Create a `tip.holder`, extending from TipHolder, that will be used to display the tip in the recyclerview.
    - Specify a constructor (where you cache all your findViewById calls)
    - and a `bindtip(Tip)` method. You can safely cast the Tip to your subclass.
    - Also add a static `onCreateViewHolder` method that will be responsible for inflating the view you want to use (don't forget to use both the layout AND the Holder)
* In the `tip.Tip` class, add a reference to your builder in property `tipsBuilders`. Order will define the order of the card, so you may want to add it somewhere else than at the end
* Finally, in `adapter.TipAdapter.onCreateViewHolder`, add a new `else` comparing the value of your class hashcode with the specified integer, and call your `onCreateViewHolder` function.
