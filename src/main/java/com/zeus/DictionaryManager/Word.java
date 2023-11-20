package com.zeus.DictionaryManager;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.awt.desktop.AppReopenedEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Word {
    private String wordTarget;
    private Description description;
    public Word() {
    }
    public Word(String wordTarget, Description description) {
        this.wordTarget = wordTarget;
        this.description = description;
    }


    @JsonAnySetter
    public void setWord(String wordTarget, Description description) {
        this.wordTarget = wordTarget;
        this.description = description;
    }

    public String getWordTarget() {
        return wordTarget;
    }
    public String getPronoun() {
        return description.getPronoun();
    }

    public void setWordTarget(String wordTarget) {
        this.wordTarget = wordTarget;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("\n\"%s\":{", wordTarget));
        result.append(description.toString());
        result.append("}");
        return result.toString();
    }

    public String print() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("\"%s\": {", wordTarget));
        result.append(description.print().replaceAll("(?m)^", "\t"));
        result.append("},\n");
        return result.toString();
    }

    public static class Description {
        private String pronoun;
        private List<Type> types;
        @JsonProperty("pronoun")
        public void setPronoun(String pronoun) {
            this.pronoun = pronoun;
        }
        @JsonProperty("type")
        public void setTypes(List<Type> types) {
            this.types = types;
        }
        @JsonAnySetter
        public void setDescription(String pronoun, List<Type> types) {
            this.pronoun = pronoun;
            this.types = types;
        }
        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            result.append(String.format("\"pronoun\":\"%s\",", pronoun));
            result.append("\"type\":[");
            if (types.size() > 1) {
                for (Type t : types) {
                    result.append(t.toString()).append(",");
                }
                result.deleteCharAt(result.length()-1);
            } else if (types.size() == 1) {
                result.append(types.get(0).toString());
            }
            result.append("]");
            return result.toString();
        }

        public String print() {
            StringBuilder result = new StringBuilder();
            result.append(String.format("\n\"pronoun\": %s\n", pronoun));
            result.append("\"type\": [\n");
            for (Type t : types) {
                result.append(t.print().replaceAll("(?m)^", "\t"));
            }
            result.append("]\n");
            return result.toString();
        }

        public String getPronoun() {
            return pronoun;
        }

        public List<Type> getTypes() {
            return types;
        }

        public static class Type {
            private String name;
            private List<Meaning> meanings;
            @JsonAnySetter
            public void setType(String name, List<Meaning> meanings) {
                this.name = name;
                this.meanings = meanings;
            }

            public void setMeanings(List<Meaning> meanings) {
                this.meanings = meanings;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<Meaning> getMeanings() {
                return meanings;
            }

            @Override
            public String toString() {
                StringBuilder result = new StringBuilder();
                result.append(String.format("{\"%s\":[", name));
                if (meanings.size() > 1) {
                    for (Meaning m : meanings) {
                        result.append(m.toString()).append(",");
                    }
                    result.deleteCharAt(result.length()-1);
                } else if (meanings.size() == 1) {
                    result.append(meanings.get(0).toString());
                }

                result.append("]}");
                return result.toString();
            }

            public String print() {
                StringBuilder result = new StringBuilder();
                result.append(String.format("\"%s\": [\n", name));
                for (Meaning m : meanings) {
                    result.append(m.print().replaceAll("(?m)^", "\t"));
                }
                result.append("],\n");
                return result.toString();
            }

            public static class Meaning {
                private String explain;
                private List<Example> examples;
                @JsonAnySetter
                public void setMeaning(String explain, List<Example> examples) {
                    this.explain = explain;
                    this.examples = examples;
                }

                public String getExplain() {
                    return explain;
                }

                public List<Example> getExamples() {
                    return examples;
                }

                @Override
                public String toString() {
                    StringBuilder result = new StringBuilder();
                    result.append(String.format("{\"%s\":[", explain));
                    if (examples.size() > 1) {
                        for (Example e : examples) {
                            result.append(e.toString()).append(",");
                        }
                        result.deleteCharAt(result.length()-1);
                    } else if (examples.size() == 1) {
                        result.append(examples.get(0).toString());
                    }
                    result.append("]}");
                    return result.toString();
                }

                public String print() {
                    StringBuilder result = new StringBuilder();
                    result.append(String.format("\"%s\": [", explain));
                    if (!examples.isEmpty()) result.append('\n');
                    for (Example e : examples) {
                        result.append(e.print().replaceAll("(?m)^", "\t"));
                    }
                    result.append("],\n");
                    return result.toString();
                }

                public static class Example {
                    private String english;
                    private String vietnamese;
                    @JsonAnySetter
                    public void setExample(String english, String vietnamese) {
                        this.english = english;
                        this.vietnamese = vietnamese;
                    }

                    public String getEnglish() {
                        return english;
                    }

                    public String getVietnamese() {
                        return vietnamese;
                    }

                    @Override
                    public String toString() {
                        return String.format("{\"%s\":\"%s\"}", english, vietnamese);
                    }

                    public String print() {
                        return String.format("\"%s\": \"%s\"\n", english, vietnamese);
                    }
                }
            }
        }
    }
}
