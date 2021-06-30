\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{my_style}
\LoadClass[12pt, a4paper]{report}

\RequirePackage{titlesec}
\RequirePackage[utf8]{vietnam}
\RequirePackage[utf8]{inputenc}
% \RequirePackage[T1]{fontenc}
% \RequirePackage{latexsym}
% \RequirePackage{amsmath}
\RequirePackage{graphicx}
\RequirePackage{xcolor}
\RequirePackage{pgf,tikz,pgfplots}
\RequirePackage{anyfontsize}
% \RequirePackage{setspace}
% \RequirePackage{geometry}
% \RequirePackage{marginnote}
% \RequirePackage{caption}
% \RequirePackage{siunitx}
% \RequirePackage{eurosym}
% \RequirePackage{colortbl}
% \RequirePackage{array}
% \RequirePackage{sectsty}
% \RequirePackage[nottoc]{tocbibind}
% \RequirePackage{float}
% \RequirePackage{wrapfig}
% \RequirePackage{vmargin}

\definecolor{Sapienza}{RGB}{130,36,51}
\definecolor{Rosso}{RGB}{232,61,61}
\definecolor{Viola}{RGB}{188,30,188}
\definecolor{Celeste}{RGB}{34,186,211}
\definecolor{Arancione}{RGB}{232,181,19}
\definecolor{Blu}{RGB}{8,144,213}
\definecolor{Azzurro}{RGB}{57,186,238}
\definecolor{Sapienza}{RGB}{111,10,25}
\definecolor{gold}{RGB}{199,153,0}
\RequirePackage{hyperref}
% \hypersetup{colorlinks=true,linkcolor=black,allbordercolors=white}
% \RequirePackage{enumitem} 
% \renewcommand\labelitemi{\textbullet} 
% \RequirePackage{pdflscape}
% \RequirePackage{pgfplots}
% \usetikzlibrary{shapes}
% \RequirePackage{pgfplots}
% \usetikzlibrary{intersections,positioning,calc}
% \RequirePackage{tkz-euclide,tkz-fct}
%     \usetkzobj{all}
% \usetikzlibrary{angles,quotes}
% \RequirePackage{lmodern}
% \RequirePackage{titlesec}
\RequirePackage{tikz}
% \usetikzlibrary{shapes}
% \usetikzlibrary{arrows,positioning}
% \RequirePackage{amsmath,bm}

% \RequirePackage{mathspec}
% \RequirePackage{xltxtra,xunicode}

\renewcommand{\maketitle}{
\thispagestyle{empty}
\begin{figure}
\vspace{-1cm}
\centering
\includegraphics[width=0.6\textwidth]{hustlogo.png}\\[1cm]
\end{figure}

\begin{center}
\textcolor{Blu}{\bfseries\large\fontsize{5mm}{7mm}\selectfont
VIỆN CÔNG NGHỆ THÔNG TIN VÀ TRUYỀN THÔNG\\Ngành khoa học máy tính\\[3cm]}
\end{center}
\begin{center}
\LARGE  \bfseries ĐỒ ÁN TỐT NGHIỆP ĐẠI HỌC\\[0.5cm] \bfseries
Sử dụng mạng Bayesian ước lượng tài nguyên khả dụng cho bài toán lập lịch thời gian thực trong môi trường điện toán đám mây \\[0.5cm] 
\end{center}

\begin{center}
\fontsize{14pt}{12pt}\selectfont
\textbf{Trương Quang Khánh} \\
\fontsize{13pt}{12pt}\selectfont
khanh.tq170083@sis.hust.edu.vn \\ [4cm]

\end{center}

\fontsize{13pt}{12pt}\selectfont
\textbf{Giảng viên hướng dẫn}:\quad  TS. Nguyễn Bình Minh \quad \begin{tikzpicture}
\draw (5, 20) -- (9, 20);
\end{tikzpicture} 

\fontsize{10pt}{12pt}\selectfont
\hspace{11cm} Chữ ký của GVHD \\

\fontsize{13pt}{12pt}\selectfont
\textbf{Bộ môn} \hspace{3.05cm}: \hspace{0.25cm}  Hệ thống Thông tin

\textbf{Viện} \hspace{3.76cm}: \hspace{0.25cm} Công nghệ thông tin và truyền thông \\ [2cm]
\begin{center}
\vfill
{\sc Hà nội, tháng 5 năm 2021}
\end{center}
\newpage
}
%
%\vspace{3cm}
%
%
%
%\vspace{-1.5cm}
%\begin{minipage}[t]{7cm}
%\begin{flushleft} \large
%\emph{Sinh viên thực hiện:}\\
%\textbf{Trương Quang Khánh  \\
%		Lớp KSTN-CNTT K62}
%\end{flushleft}
%\end{minipage}
%~
%\begin{minipage}[t]{8cm}
%\begin{flushleft} \large
%\emph{Giảng viên hướng dẫn: } \\
%\textbf{TS. Nguyễn Bình Minh \\
%		Bộ môn Hệ thống Thông tin}
%\end{flushleft}
%\end{minipage}


\titleformat{\chapter}[display]
  {\LARGE\normalfont\em\bfseries\color{Blu}}
  {\filleft
  \begin{tikzpicture}
  \node[
    outer sep=0pt,
    text width=2.5cm,
    minimum height=3cm,
    fill=white,
    font=\color{Blu}\fontsize{50}{0}\selectfont,
    align=center
    ] (num) {\thechapter};
  \node[
    rotate=90,
    anchor=south,
    font=\color{black}\large\normalfont
    ] at ([xshift=-5pt]num.west) {\Huge\textsc{}};  
  \end{tikzpicture}%
    }{12pt}
    {}
    [\titlerule]

    \newcommand*\numb[1]{%
\begin{tikzpicture}[baseline=-0.7ex]
\node[
  outer sep=0pt,
      text width=0.6cm,
      minimum height=0.6cm,
      fill=Blu,
      font=\color{white}\fontsize{12}{20}\selectfont,
      align=center
      ] (num) {\thesection};
\end{tikzpicture}%
}
\titleformat{\section}
  {\normalfont\color{Blu}\large\sc\sffamily}{\numb{\thesection}}{0.8em}{}

%--Intestazione subsubsection--
\newcommand*\stocaz[1]{%
\begin{tikzpicture}[baseline=-0.7ex]
\node[
  outer sep=0pt,
      text width=0.8cm,
      minimum height=0.5cm,
      fill=Blu,
      font=\color{white}\fontsize{10}{20}\selectfont,
      align=center
      ] (num) {\thesubsection};
\end{tikzpicture}%
}
\titleformat{\subsection}
  {\normalfont\color{Blu}\bfseries\sc\sffamily}{\stocaz{\thesubsection}}{0.8em}{}
  
  
\newcommand*\subsubsec[1]{%
\begin{tikzpicture}[baseline=-0.7ex]
\node[
  outer sep=0pt,
      text width=1.1cm,
      minimum height=0.5cm,
      fill=Blu,
      font=\color{white}\fontsize{10}{20}\selectfont,
      align=center
      ] (num) {\thesubsubsection};
\end{tikzpicture}%
}
\titleformat{\subsubsection}
  {\normalfont\color{Blu}\bfseries\sc\sffamily}{\subsubsec{\thesubsubsection}}{0.8em}{}
