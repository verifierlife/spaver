# spaver
spaver: a spatial verifier for S4u

# S4$_u$ 语法

$$\tau ::= p \mid \overline{\tau} \mid \tau_1\sqcap\tau_2 \mid \tau_1\sqcup\tau_2 \mid \mathbb{I}\tau \mid \mathbb{C}\tau$$

$$\varphi ::= \tau_1\sqsubseteq\tau_2 \mid \neg\varphi \mid \varphi_1\wedge\varphi_2 \mid \varphi_1\vee\varphi_2$$

 - $\tau$ is a spatial term
 - $p$ is a atomic proposition
 - $\sqcap$ and $\sqcup$ denotes spatial \textit{intersection} and \textit{Union}
 - $\mathbb{I}$ and $\mathbb{C}$ means spatial \textit{interior} and \textit{closure}
 - $\sqsubseteq$ expresses the spatial subset relation
 - $\neg$, $\wedge$ and $\vee$ are Boolean operators 
