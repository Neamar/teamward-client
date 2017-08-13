## How to help translating the app?

You wanna help translating? That's great :) Thanks!

Head over to https://github.com/Neamar/teamward-client/tree/master/app/src/main/res, and look for a folder named `values-{your-language-code}`. For instance, the french folder this will be `values-fr`.

In this folder, you'll find a file named `strings.xml`. Click on the file title, then click the pencil button ("Edit this file"). From there, you'll be able to define all the translation for this language. Do not touch anything between `<` and `>`. For instance:

```xml
    <string name="champion_image">Champion image</string>
```

You only want to translate the `Champion image` part; everything else should stay the same:

```xml
    <string name="champion_image">YOUR TRANSLATION</string>
```

Sometimes, strings will have placeholders (either `%s` or `%1$s`, for instance `<string name="s_has_no_flash">%1$s uses %2$s and %3$s.</string>`). You'll need to have the same number of placeholders, but you can change the order if you need to.

Once you're done, click on "create a pull request", explain what changed and click save; you're done!

Thanks :)
