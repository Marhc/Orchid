---
---

{% extends '_wikiBase' %}

{% block sectionIntro %}

The `script` Publisher allows you to run arbitrary shell commands as part of your Orchid deploy pipeline. If the shell
script execution fails, so does the whole pipeline. 

Configuration is simple, just pass it a command, either as a String or an array of strings, and optionally provide it
a specific directory to run from. If no `cwd` is given, it defaults to your resources directory.

**Example configuration**

{% highlight 'yaml' %}
services:
  publications: 
    stages: 
      - type: script
        command: './deploy.sh'
      - type: script 
        command:
          - './deploy.sh' 
          - 'arg1' 
          - 'arg2'
        cwd: '~/orchid/deploy/scripts'
{% endhighlight %}

{% endblock %}
