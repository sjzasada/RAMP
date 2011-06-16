#!/usr/bin/perl
#

use Sys::Hostname;

$myname=hostname;

$timefile="/tmp/".$myname.".timefile";
$logname="ANL-Intrepid-2009-1.swf";

$now=time;

if (-e $timefile) {

open FILE, "<$timefile" or die $!;

my @lines = <FILE>;

$start=$lines[0];
close FILE;

} else {
open FILE, ">$timefile" or die $!; 
print FILE $now; 
close FILE;
$start=$now;
}

$deltatime=$now-$start;

print "Job id    \tName\tTSK\tReq Time\tS\n";
print "----------\t----\t---\t--------\t-\n";

open FILE2, "<$logname" or die $!;

while (my $line = <FILE2>) {

  if ($line =~ /^\s*$|^;/) {
	next;
} else {

    $line =~ /^\s*(.*)\s*$/;
    my $data = $1;


    my ($job, $sub, $wait, $t, $p, $cpu, $mem, $preq, $treq, $mreq,
	$status, $u, $gr, $app, $q, $part, $prec, $think) = split(/\s+/,$data);

	$endtime = $sub+$wait+$t;


	#print "sub=".$sub." end=".$endtime." delta=".$deltatime."\n";

	if ($sub < $deltatime) {

	if ($endtime > $deltatime) {

		if ($sub+$wait > $deltatime) {
		$stat="R";
} else {
$stat="Q";
}

		print $myname.".".$job."\t".$u."\t".$p."\t".$treq."      \t".$stat."\n";
		}
	} else {
		last;
	}

}

}


close FILE2;
