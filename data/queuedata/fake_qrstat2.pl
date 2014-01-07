#!/usr/bin/perl
#

use Sys::Hostname;

($when, $jobsize, $timefile)=@ARGV;

$myname=hostname;


$now=time;


if (-e $timefile) {

open FILE, "<$timefile" or die $!;

my @lines = <FILE>;

$start=$lines[0];
$offset=$lines[1];
$corecount=$lines[2];
$logname=$lines[3];
close FILE;

} else {
open FILE, ">$timefile" or die $!; 
print FILE $now; 
close FILE;
$start=$now;
}

if (!defined $offset || $offset=="") {
    $offset=0;
}

$deltatime=$now-$start+$offset;

#print "delta time is ".$deltatime."\n";

#print "offset is ".$offset."\n";


#read file

open FILE2, "<$logname" or die $!;

$totalcores=0;

while (my $line = <FILE2>) {

  if ($line =~ /^\s*$|^;/) {
	next;
} else {

    $line =~ /^\s*(.*)\s*$/;
    my $data = $1;


    my ($job, $sub, $wait, $t, $p, $cpu, $mem, $preq, $treq, $mreq,
	$status, $u, $gr, $app, $q, $part, $prec, $think) = split(/\s+/,$data);

	$endtime = $sub+$wait+$t;


    if (($sub == -1) || ($t == -1) || ($p == -1)) {
        # something very fishy: job arrival, runtime, or processors undefined.
        next;
    }

    $interestedin=$offset+$when;

	if ($sub < $deltatime) {
	    if ($endtime > $deltatime) {
		if ($sub > $interestedin) {
#		    print $data."\n";
		    $totalcores=$totalcores+$p;
		}
	    }
	} else {
		last;
	}

}


}

#print "Total cores allocated = ".$totalcores."\n";

$percent=$totalcores/$corecount;

#print "Percent allocated = ".$percent."\n";

if ($jobsize < ($corecount-$totalcores)) {
    $resid=int(rand(899999));
    $resid=$resid+100000;
    print $resid."\n";
} else {
    print "Fail\n";
}

close FILE2;
